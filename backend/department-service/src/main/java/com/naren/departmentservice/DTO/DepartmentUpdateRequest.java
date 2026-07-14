package com.naren.departmentservice.DTO;

import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(
        @Size(max = 100, message = "Department name must not exceed 100 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Size(max = 200, message = "Location must not exceed 200 characters")
        String location,

        Long managerId
) {
}
