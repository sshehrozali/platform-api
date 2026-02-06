package com.platform.api;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "provision")
public class Provision {

    public Provision() {
    }

    public Provision(UUID provisionId, String state, String createdBy) {
        this.provisionId = provisionId;
        this.provisionState = state;
        this.createdBy = createdBy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private UUID provisionId;

    @Column(nullable = false)
    private String provisionState;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "cluster_id")
    private String clusterId;

    @Column(name = "cluster_name")
    private String clusterName;

    @Column(name = "db_instance_name")
    private String dbInstanceName;

    @Column(name = "db_connection_name")
    private String dbConnectionName;

    public void setProvisionState(String provisionState) {
        this.provisionState = provisionState;
    }

    public String getProvisionState() {
        return provisionState;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setDbInstanceName(String dbInstanceName) {
        this.dbInstanceName = dbInstanceName;
    }

    public String getDbInstanceName() {
        return dbInstanceName;
    }

    public void setDbConnectionName(String dbConnectionName) {
        this.dbConnectionName = dbConnectionName;
    }

    public String getDbConnectionName() {
        return dbConnectionName;
    }
}
