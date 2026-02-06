package com.platform.api;

import com.platform.Provision;
import com.platform.api.dto.ProvisionState;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProvisionService {

    private final ProvisionRespository provisionRespository;
    private final PulumiRunner pulumiRunner;

    public ProvisionService(ProvisionRespository provisionRespository, PulumiRunner pulumiRunner) {
        this.provisionRespository = provisionRespository;
        this.pulumiRunner = pulumiRunner;
    }

    public UUID create(String size, String clusterName, Integer nodeCount, String dbEngine, String createdBy) {
        var provisionId = UUID.randomUUID();
        provisionRespository.save(new Provision(provisionId, ProvisionState.IN_PROGRESS));

        pulumiRunner.createGKECluster(
                provisionId, clusterName, "cf", nodeCount, createdBy
        );

        return provisionId;
    }
}
