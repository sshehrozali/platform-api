package com.platform.api.dto;

public record ProvisionStatusResponse(
        String status,
        String created_by,
        String cluster_id,
        String cluster_name,
        String db_instance_name,
        String db_connection_name
) {
}
