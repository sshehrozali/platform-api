package com.platform.api;

import com.pulumi.Context;
import com.pulumi.gcp.container.Cluster;
import com.pulumi.gcp.container.ClusterArgs;
import com.pulumi.gcp.container.inputs.ClusterNodeConfigArgs;
import com.pulumi.gcp.sql.DatabaseInstance;
import com.pulumi.gcp.sql.DatabaseInstanceArgs;
import com.pulumi.gcp.sql.inputs.DatabaseInstanceSettingsArgs;
import org.springframework.stereotype.Service;

@Service
public class InfrastructureCode {

    public void buildGKEAndCloudSql(Context context,
                                    String clusterName,
                                    String computeFamily,
                                    Integer nodeCount,
                                    String dbName,
                                    String dbEngine,
                                    String dbTier) {
        var cluster = new Cluster(clusterName, ClusterArgs.builder()
                .initialNodeCount(nodeCount)
                .nodeConfig(ClusterNodeConfigArgs.builder()
                        .machineType(computeFamily)
                        .oauthScopes("https://www.googleapis.com/auth/cloud-platform")
                        .build())
                .build());

        var db = new DatabaseInstance(dbName + "-db", DatabaseInstanceArgs.builder()
                .databaseVersion(dbEngine)
                .region("us-central1")
                .settings(DatabaseInstanceSettingsArgs.builder()
                        .tier(dbTier)
                        .build())
                .build());

        context.export("cluster-id", cluster.id());
        context.export("cluster-name", cluster.name());
        context.export("db-instance-name", db.name());
        context.export("db-connection-name", db.connectionName());
    }
}
