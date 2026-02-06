package com.platform.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvisionRespository extends JpaRepository<Provision, Long> {
    Provision findByProvisionId(UUID provisionId);
}
