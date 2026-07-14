package com.naren.departmentservice.DTO;

import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        String name,
        String description,
        String location,
        Long managerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
