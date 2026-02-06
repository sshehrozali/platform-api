package com.platform.api.dto;

public record ProvisionNewRequest(
        String size,
        String cluster_name,
        Integer node_count,
        String db_engine,
        String created_by
) {
}
