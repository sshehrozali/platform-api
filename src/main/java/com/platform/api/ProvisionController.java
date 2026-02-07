package com.platform.api;

import com.platform.api.dto.ProvisionNewRequest;
import com.platform.api.dto.ProvisionNewResponse;
import com.platform.api.dto.ProvisionState;
import com.platform.api.dto.ProvisionStatusResponse;
import com.pulumi.automation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("v1/provision")
public class ProvisionController {

    private final ProvisionService provisionService;

    public ProvisionController(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProvisionNewResponse> create(@RequestBody @Valid ProvisionNewRequest request) throws AutomationException {
        var id = provisionService.create(request.size(),
                request.cluster_name(),
                request.node_count(),
                request.db_name(),
                request.db_engine(),
                request.created_by());

        return new ResponseEntity<ProvisionNewResponse>(new ProvisionNewResponse(id.toString(),
                ProvisionState.IN_PROGRESS.name(),
                Instant.now().toString()),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> status(@PathVariable UUID id) {
        return provisionService.getByProvisionId(id)
                .map(p -> ResponseEntity.ok(new ProvisionStatusResponse(
                        p.getProvisionState(),
                        p.getCreatedBy(),
                        p.getClusterId(),
                        p.getClusterName(),
                        p.getDbInstanceName(),
                        p.getDbConnectionName()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

}
