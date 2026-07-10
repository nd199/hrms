package com.naren.humanresourcemanagement.Service.Impl;

import com.naren.humanresourcemanagement.DTO.EmployeeCreateRequest;
import com.naren.humanresourcemanagement.DTO.EmployeeResponse;
import com.naren.humanresourcemanagement.DTO.EmployeeUpdateRequest;
import com.naren.humanresourcemanagement.DTO.mapper.EmployeeMapper;
import com.naren.humanresourcemanagement.Entity.Employee;
import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import com.naren.humanresourcemanagement.Exception.ResourceNotFoundException;
import com.naren.humanresourcemanagement.Repository.EmployeeRepository;
import com.naren.humanresourcemanagement.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("employeeQueryService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeQService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Employee not found with id: " + id)
                );
        return employeeMapper.apply(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Employee not found with code: " + employeeCode)
                );
        return employeeMapper.apply(employee);
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(employeeMapper);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(employeeMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByStatus(EmploymentStatus status) {
        return employeeRepository.findByEmploymentStatus(status)
                .stream()
                .map(employeeMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByManager(Long managerId) {
        return employeeRepository.findByManagerId(managerId)
                .stream()
                .map(employeeMapper)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String keyword, Pageable pageable) {
        return employeeRepository.searchEmployees(keyword, pageable).map(employeeMapper);
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        throw new UnsupportedOperationException("Use employeeCrudService for create operations");
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        throw new UnsupportedOperationException("Use employeeCrudService for update operations");
    }

    @Override
    public void deleteEmployee(Long id) {
        throw new UnsupportedOperationException("Use employeeCrudService for delete operations");
    }
}
