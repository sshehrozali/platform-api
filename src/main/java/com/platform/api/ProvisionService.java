package com.platform.api;

import com.platform.api.dto.ProvisionState;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProvisionService {

    private final ProvisionRespository provisionRespository;
    private final PulumiRunner pulumiRunner;

    public ProvisionService(ProvisionRespository provisionRespository, PulumiRunner pulumiRunner) {
        this.provisionRespository = provisionRespository;
        this.pulumiRunner = pulumiRunner;
    }

    public UUID create(String size, String clusterName, Integer nodeCount, String dbName, String dbEngine, String createdBy) {
        var provisionId = UUID.randomUUID();
        provisionRespository.save(new Provision(provisionId, ProvisionState.IN_PROGRESS.name(), createdBy));

        var cf = mapToComputeEngine(size);
        var dbTier = mapToDbInstanceTier(size);

        // Submit Pulumi IaC job in background
        pulumiRunner.createGKEAndCloudSql(
                provisionId, clusterName, cf, nodeCount, dbName, dbEngine, dbTier, createdBy
        );

        return provisionId;
    }

    public Optional<Provision> getByProvisionId(UUID provisionId) {
        return Optional.ofNullable(provisionRespository.findByProvisionId(provisionId));
    }

    private String mapToComputeEngine(String size) {
        return switch (size) {
            case "small"  -> "e2-small";    // 2 vCPU, 2 GiB
            case "medium" -> "e2-medium";   // 2 vCPU, 4 GiB
            case "large"  -> "e2-standard-4"; // 4 vCPU, 16 GiB
            default       -> "e2-medium";
        };
    }

    private String mapToDbInstanceTier(String size) {
        return switch (size) {
            case "small"  -> "db-f1-micro";   // shared-core
            case "medium" -> "db-g1-small";   // shared-core, more CPU/RAM
            case "large"  -> "db-custom-2-7680"; // 2 vCPU, ~7.5 GiB (example custom)
            default       -> "db-g1-small";
        };
    }
}
