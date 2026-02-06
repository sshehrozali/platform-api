package com.platform.api;

import com.platform.api.dto.ProvisionState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProvisionService {

    // Replace with real DB
    private final Map<UUID, ProvisionState> IN_MEMORY_STORE = new HashMap<>();
    private final PulumiRunner pulumiRunner;

    public ProvisionService(PulumiRunner pulumiRunner) {
        this.pulumiRunner = pulumiRunner;
    }

    public UUID create(String size, String clusterName, Integer nodeCount, String dbEngine, String createdBy) {
        // 1. validate parameters first!
        // 2. pick the correct Compute machine based on size and nodeCount
        // 3. save initial state in db
        // 4. submit provision task to pulumi queue in background
        // 5. return

        var provisionId = UUID.randomUUID();
        IN_MEMORY_STORE.put(provisionId, ProvisionState.IN_PROGRESS);

        pulumiRunner.createGKECluster(
                clusterName, "cf", nodeCount, createdBy
        );

        return provisionId;
    }
}
