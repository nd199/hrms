package com.naren.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateRequest(
        @NotBlank(message = "Role name is required")
        @Size(max = 50, message = "Role name must not exceed 50 characters")
        String name
) {
}
