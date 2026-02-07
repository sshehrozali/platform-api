package com.platform.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProvisionNewRequest(
        @NotBlank String size,
        @NotBlank String cluster_name,
        @NotNull @Min(1) Integer node_count,
        @NotBlank String db_name,
        @NotBlank String db_engine,
        @NotBlank String created_by
) {
}
