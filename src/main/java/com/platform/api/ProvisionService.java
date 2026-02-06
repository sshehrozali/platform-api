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

        var cf = mapToComputeEngine(size);
        pulumiRunner.createGKECluster(
                provisionId, clusterName, cf, nodeCount, createdBy
        );

        return provisionId;
    }

    private String mapToComputeEngine(String size) {
        return switch (size) {
            case "small"  -> "e2-small";    // 2 vCPU, 2 GiB
            case "medium" -> "e2-medium";   // 2 vCPU, 4 GiB
            case "large"  -> "e2-standard-4"; // 4 vCPU, 16 GiB
            default       -> "e2-medium";
        };
    }
}
