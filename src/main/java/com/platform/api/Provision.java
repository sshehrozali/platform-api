package com.platform.api;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "provision")
public class Provision {

    public Provision() {
    }

    public Provision(UUID provisionId, String state) {
        this.provisionId = provisionId;
        this.provisionState = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private UUID provisionId;

    @Column(nullable = false)
    private String provisionState;

    public void setProvisionState(String provisionState) {
        this.provisionState = provisionState;
    }

    public String getProvisionState() {
        return provisionState;
    }
}
