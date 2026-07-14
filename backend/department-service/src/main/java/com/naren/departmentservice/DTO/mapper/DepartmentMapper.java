package com.naren.departmentservice.DTO.mapper;

import com.naren.departmentservice.DTO.DepartmentResponse;
import com.naren.departmentservice.Entity.Department;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DepartmentMapper implements Function<Department, DepartmentResponse> {

    @Override
    public DepartmentResponse apply(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getLocation(),
                department.getManagerId(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }
}
