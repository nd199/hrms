package com.naren.humanresourcemanagement.Service;

import com.naren.humanresourcemanagement.DTO.EmployeeCreateRequest;
import com.naren.humanresourcemanagement.DTO.EmployeeResponse;
import com.naren.humanresourcemanagement.DTO.EmployeeUpdateRequest;
import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request);

    void deleteEmployee(Long id);

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse getEmployeeByCode(String employeeCode);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    List<EmployeeResponse> getEmployeesByDepartment(Long departmentId);

    List<EmployeeResponse> getEmployeesByStatus(EmploymentStatus status);

    List<EmployeeResponse> getEmployeesByManager(Long managerId);

    Page<EmployeeResponse> searchEmployees(String keyword, Pageable pageable);
}

