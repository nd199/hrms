package com.naren.employeeservice.DTO;

import com.naren.employeeservice.Entity.Enums.EmploymentStatus;
import com.naren.employeeservice.Entity.Enums.Gender;

import java.time.LocalDateTime;

public record EmployeeResponse(
        Long id,
        String employeeCode,
        String firstName,
        String lastName,
        String email,
        String phone,
        Gender gender,
        LocalDateTime dateOfBirth,
        LocalDateTime hireDate,
        EmploymentStatus employmentStatus,
        String address,
        String city,
        String state,
        String country,
        String postalCode,
        String emergencyContactName,
        String emergencyContactPhone,
        Long departmentId,
        Long positionId,
        Long managerId,
        Long shiftId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
