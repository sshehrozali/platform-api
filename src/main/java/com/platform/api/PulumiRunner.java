package com.platform.api;

import com.platform.api.dto.ProvisionState;
import com.pulumi.automation.*;
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

    private final ExecutorService runner = Executors.newFixedThreadPool(8);

    public PulumiRunner(InfrastructureCode infrastructureCode, ProvisionRespository provisionRespository) {
        this.infrastructureCode = infrastructureCode;
        this.provisionRespository = provisionRespository;
    }

    public void createGKEAndCloudSql(UUID provisionId, String clusterName, String computeFamily, Integer nodeCount, String dbName, String dbEngine, String dbTier, String createdBy) {
        runner.submit(() -> {
            logger.info("[{}]: queuing new task to create GKE={} and CloudSQL={} {} by {}...", provisionId, clusterName, dbName, dbEngine, createdBy);
            try {
                var stack = LocalWorkspace.createOrSelectStack("test",
                        "dev", context -> infrastructureCode.buildGKEAndCloudSql(context, clusterName, computeFamily, nodeCount, dbName, dbEngine, dbTier));

                stack.setConfig("gcp:project", new ConfigValue("authlete-techexe-20260205-01"));
                stack.setConfig("gcp:region", new ConfigValue("us-central1"));

                try {
                    logger.info("[{}]: running pulumi refresh...", provisionId);
                    stack.refresh(RefreshOptions.builder()
                            .onStandardOutput(output -> logger.info("[pulumi refresh]: {}", output))
                            .build());
                } catch (Exception e) {
                    logger.warn("[{}]: pulumi refresh failed (continuing with up): {}", provisionId, e.getMessage());
                }

                // Run 'up'
                logger.info("[{}]: running pulumi up...", provisionId);
                UpResult result = stack.up(UpOptions.builder()
                        .onStandardOutput(output -> logger.info("[pulumi]: {}", output))
                        .build());

                logger.info("[{}]: pulumi up finished!  \n{} {}",
                        provisionId, result.outputs().get("cluster-id").value(),
                        result.outputs().get("cluster-name").value());

                var provisionRecord = provisionRespository.findByProvisionId(provisionId);
                provisionRecord.setProvisionState(ProvisionState.READY);
                provisionRespository.save(provisionRecord);

                logger.info("provision id = {} marked as {} in db", provisionId.toString(), provisionRecord.getProvisionState().toString());

            } catch (Exception e) {
                logger.error("[{}]: Pulumi failed: {} - {}", provisionId, e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : "no cause", e);
                throw new PulumiRunnerException("Error while running pulumi: ", e);
            }
        });
    }
}
