package com.naren.departmentservice.Service.Impl;

import com.naren.departmentservice.DTO.*;
import com.naren.departmentservice.DTO.mapper.DepartmentMapper;
import com.naren.departmentservice.Entity.Department;
import com.naren.departmentservice.Exception.ResourceNotFoundException;
import com.naren.departmentservice.Repository.DepartmentRepository;
import com.naren.departmentservice.Service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("departmentQueryService")
@Transactional(readOnly = true)
public class DepartmentQService implements DepartmentService {

    private final DepartmentRepository repo;
    private final DepartmentMapper mapper;

    public DepartmentQService(DepartmentRepository repo, DepartmentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapper.apply(department);
    }

    @Override
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
        return repo.findAll(pageable).map(mapper);
    }

    @Override
    public Page<DepartmentResponse> searchDepartments(String keyword, Pageable pageable) {
        return repo.searchDepartments(keyword, pageable).map(mapper);
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        throw new UnsupportedOperationException("Use departmentCrudService for create operations");
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentUpdateRequest request) {
        throw new UnsupportedOperationException("Use departmentCrudService for update operations");
    }

    @Override
    public void deleteDepartment(Long id) {
        throw new UnsupportedOperationException("Use departmentCrudService for delete operations");
    }
}
