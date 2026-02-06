package com.platform.api;

import com.pulumi.Context;
import com.pulumi.gcp.container.Cluster;
import com.pulumi.gcp.container.ClusterArgs;
import com.pulumi.gcp.container.inputs.ClusterNodeConfigArgs;
import com.pulumi.gcp.sql.DatabaseInstance;
import com.pulumi.gcp.sql.DatabaseInstanceArgs;
import com.pulumi.gcp.sql.inputs.DatabaseInstanceSettingsArgs;
import com.pulumi.gcp.sql.inputs.DatabaseInstanceSettingsIpConfigurationArgs;

public class InfrastructureService {

    public void execute(Context context) {
        // 1. GKE Cluster
        var cluster = new Cluster("platform-gke", ClusterArgs.builder()
                .initialNodeCount(2)
                .nodeConfig(ClusterNodeConfigArgs.builder()
                        .machineType("e2-medium")
                        .oauthScopes("https://www.googleapis.com/auth/cloud-platform")
                        .build())
                .build());

//        // 2. Cloud SQL (Postgres)
//        var sql = new DatabaseInstance("platform-db", DatabaseInstanceArgs.builder()
//                .settings(DatabaseInstanceSettingsArgs.builder()
//                        .tier("db-f1-micro")
//                        .ipConfiguration(DatabaseInstanceSettingsIpConfigurationArgs.builder()
//                                .ipv4Enabled(false)
//                                .privateNetwork("projects/YOUR_PROJECT/global/networks/default")
//                                .build())
//                        .build())
//                .build());
    }
}
