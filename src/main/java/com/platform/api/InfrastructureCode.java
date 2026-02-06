package com.platform.api;

import com.pulumi.Context;
import com.pulumi.gcp.container.Cluster;
import com.pulumi.gcp.container.ClusterArgs;
import com.pulumi.gcp.container.inputs.ClusterNodeConfigArgs;
import org.springframework.stereotype.Service;

@Service
public class InfrastructureCode {

    public void buildGKECluster(Context context, String clusterName, String computeFamily, Integer nodeCount) {
        var cluster = new Cluster(clusterName, ClusterArgs.builder()
                .initialNodeCount(nodeCount)
                .nodeConfig(ClusterNodeConfigArgs.builder()
                        .machineType(computeFamily)
                        .oauthScopes("https://www.googleapis.com/auth/cloud-platform")
                        .build())
                .build());

        context.export("cluster-id", cluster.id());
        context.export("cluster-name", cluster.name());
    }
}
