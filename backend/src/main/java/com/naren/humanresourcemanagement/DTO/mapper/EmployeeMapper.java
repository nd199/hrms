package com.naren.humanresourcemanagement.DTO.mapper;

import com.naren.humanresourcemanagement.DTO.EmployeeResponse;
import com.naren.humanresourcemanagement.Entity.Employee;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EmployeeMapper implements Function<Employee, EmployeeResponse> {

    @Override
    public EmployeeResponse apply(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getGender(),
                employee.getDateOfBirth(),
                employee.getHireDate(),
                employee.getEmploymentStatus(),
                employee.getAddress(),
                employee.getCity(),
                employee.getState(),
                employee.getCountry(),
                employee.getPostalCode(),
                employee.getEmergencyContactName(),
                employee.getEmergencyContactPhone(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getDepartment() != null ? employee.getDepartment().getName() : null,
                employee.getPosition() != null ? employee.getPosition().getId() : null,
                employee.getPosition() != null ? employee.getPosition().getTitle() : null,
                employee.getManager() != null ? employee.getManager().getId() : null,
                employee.getManager() != null ?
                        employee.getManager().getFirstName() + " " + employee.getManager().getLastName() : null,
                employee.getShift() != null ? employee.getShift().getId() : null,
                employee.getShift() != null ? employee.getShift().getName() : null,
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
