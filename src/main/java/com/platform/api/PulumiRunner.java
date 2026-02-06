package com.platform.api;

import com.platform.api.dto.ProvisionState;
import com.pulumi.automation.ConfigValue;
import com.pulumi.automation.LocalWorkspace;
import com.pulumi.automation.UpOptions;
import com.pulumi.automation.UpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PulumiRunner {

    private final Logger logger = LoggerFactory.getLogger(PulumiRunner.class);
    private final InfrastructureCode infrastructureCode;
    private final ProvisionRespository provisionRespository;

    private final ExecutorService runner = Executors.newSingleThreadExecutor();

    public PulumiRunner(InfrastructureCode infrastructureCode, ProvisionRespository provisionRespository) {
        this.infrastructureCode = infrastructureCode;
        this.provisionRespository = provisionRespository;
    }

    public void createGKECluster(UUID provisionId, String clusterName, String computeFamily, Integer nodeCount, String createdBy) {
        runner.submit(() -> {
            logger.info("queuing new task to create GKE cluster {} by {}...", clusterName, createdBy);
            try {
                var stack = LocalWorkspace.createOrSelectStack("test",
                        "dev", context -> infrastructureCode.buildGKECluster(context, clusterName, computeFamily, nodeCount));

                stack.setConfig("gcp:project", new ConfigValue("authlete-techexe-20260205-01"));
                stack.setConfig("gcp:region", new ConfigValue("us-central1"));

                // Run 'up'
                logger.info("running pulumi up for GKE cluster creation = {}", clusterName);
                UpResult result = stack.up(UpOptions.builder()
                        .onStandardOutput(output -> System.out.println())
                        .build());

                logger.info("GKE cluster {} successfully provisioned\n{} {}",
                        clusterName, result.outputs().get("cluster-id"),
                        result.outputs().get("cluster-name"));

                var provisionRecord = provisionRespository.findByProvisionId(provisionId);
                provisionRecord.setProvisionState(ProvisionState.READY);
                provisionRespository.save(provisionRecord);

                logger.info("provision id = {} marked as {} in db", provisionId.toString(), provisionRecord.getProvisionState().toString());

            } catch (Exception e) {
                throw new PulumiRunnerException("Error while running pulumi: ", e);
            }
        });
    }
}
