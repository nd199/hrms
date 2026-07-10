package com.naren.humanresourcemanagement.DTO;

import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import com.naren.humanresourcemanagement.Entity.Enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDateTime dateOfBirth;
    private LocalDateTime hireDate;
    private EmploymentStatus employmentStatus;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionTitle;
    private Long managerId;
    private String managerName;
    private Long shiftId;
    private String shiftName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
