package com.naren.authservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean enabled,
        Boolean locked,
        Set<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
