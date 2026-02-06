package com.platform.api;

import com.platform.api.dto.ProvisionNewRequest;
import com.platform.api.dto.ProvisionNewResponse;
import com.platform.api.dto.ProvisionState;
import com.pulumi.automation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("v1/provision")
public class ProvisionController {

    private final ProvisionService provisionService;

    public ProvisionController(ProvisionService provisionService) {
        this.provisionService = provisionService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProvisionNewResponse> create(@RequestBody ProvisionNewRequest request) throws AutomationException {
        var id = provisionService.create(request.size(),
                request.cluster_name(),
                request.node_count(),
                request.db_engine(),
                request.created_by());

        return new ResponseEntity<ProvisionNewResponse>(new ProvisionNewResponse(id.toString(),
                ProvisionState.IN_PROGRESS.name(),
                Instant.now().toString()),
                HttpStatus.ACCEPTED);
    }

}
