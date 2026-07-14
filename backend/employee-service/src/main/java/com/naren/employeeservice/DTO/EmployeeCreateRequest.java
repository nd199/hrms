package com.naren.employeeservice.DTO;

import com.naren.employeeservice.Entity.Enums.EmploymentStatus;
import com.naren.employeeservice.Entity.Enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EmployeeCreateRequest(
        @NotBlank(message = "Employee code is required")
        @Size(max = 20, message = "Employee code must not exceed 20 characters")
        String employeeCode,

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        Gender gender,

        LocalDateTime dateOfBirth,

        @NotNull(message = "Hire date is required")
        LocalDateTime hireDate,

        @NotNull(message = "Employment status is required")
        EmploymentStatus employmentStatus,

        @Size(max = 200, message = "Address must not exceed 200 characters")
        String address,

        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Size(max = 100, message = "State must not exceed 100 characters")
        String state,

        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
        String emergencyContactName,

        @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
        String emergencyContactPhone,

        @NotNull(message = "Department ID is required")
        Long departmentId,

        Long positionId,

        Long managerId,

        Long shiftId
) {
}
