package com.naren.employeeservice.DTO.mapper;

import com.naren.employeeservice.DTO.EmployeeResponse;
import com.naren.employeeservice.Entity.Employee;
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
                employee.getDepartmentId(),
                employee.getPositionId(),
                employee.getManagerId(),
                employee.getShiftId(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }
}
