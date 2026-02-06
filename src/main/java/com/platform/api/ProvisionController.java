package com.platform.api;

import com.platform.api.dto.ProvisionNewRequest;
import com.platform.api.dto.ProvisionNewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/provision")
public class ProvisionController {

    @PostMapping("/new")
    public ResponseEntity<ProvisionNewResponse> create(@RequestBody ProvisionNewRequest request) {
        return ResponseEntity.ok(new ProvisionNewResponse("id", "status", "timestamp"));
    }

}
