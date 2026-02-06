package com.platform;

import com.platform.api.dto.ProvisionState;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "provision")
public class Provision {

    public Provision() {
    }

    public Provision(UUID provisionId, ProvisionState state) {
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
    private ProvisionState provisionState;

    public void setProvisionState(ProvisionState provisionState) {
        this.provisionState = provisionState;
    }

    public ProvisionState getProvisionState() {
        return provisionState;
    }
}
