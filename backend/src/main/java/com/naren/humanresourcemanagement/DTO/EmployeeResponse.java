package com.naren.humanresourcemanagement.DTO;

import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import com.naren.humanresourcemanagement.Entity.Enums.Gender;

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
        String departmentName,
        Long positionId,
        String positionTitle,
        Long managerId,
        String managerName,
        Long shiftId,
        String shiftName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
