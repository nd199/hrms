package com.naren.employeeservice.Service.Impl;

import com.naren.employeeservice.DTO.*;
import com.naren.employeeservice.DTO.mapper.EmployeeMapper;
import com.naren.employeeservice.Entity.Employee;
import com.naren.employeeservice.Entity.Enums.EmploymentStatus;
import com.naren.employeeservice.Exception.ResourceNotFoundException;
import com.naren.employeeservice.Repository.EmployeeRepository;
import com.naren.employeeservice.Service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("employeeQueryService")
@Transactional(readOnly = true)
public class EmployeeQService implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeQService.class);

    private final EmployeeRepository repo;
    private final EmployeeMapper mapper;

    public EmployeeQService(EmployeeRepository repo, EmployeeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }


    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapper.apply(employee);
    }

    @Override
    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        Employee employee = repo.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return mapper.apply(employee);
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return repo.findAll(pageable).map(mapper);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {
        return repo.findByDepartmentId(departmentId)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByStatus(EmploymentStatus status) {
        return repo.findByEmploymentStatus(status)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponse> getEmployeesByManager(Long managerId) {
        return repo.findByManagerId(managerId)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String keyword, Pageable pageable) {
        return repo.searchEmployees(keyword, pageable).map(mapper);
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
