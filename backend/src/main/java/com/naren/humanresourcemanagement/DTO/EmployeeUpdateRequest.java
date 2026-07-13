package com.naren.humanresourcemanagement.DTO;

import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import com.naren.humanresourcemanagement.Entity.Enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EmployeeUpdateRequest(
        @Size(max = 20, message = "Employee code must not exceed 20 characters")
        String employeeCode,

        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        Gender gender,

        LocalDateTime dateOfBirth,

        LocalDateTime hireDate,

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

        Long departmentId,

        Long positionId,

        Long managerId,

        Long shiftId
) {
}
