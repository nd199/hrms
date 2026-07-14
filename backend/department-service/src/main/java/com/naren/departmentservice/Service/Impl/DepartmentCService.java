package com.naren.departmentservice.Service.Impl;

import com.naren.departmentservice.DTO.*;
import com.naren.departmentservice.DTO.mapper.DepartmentMapper;
import com.naren.departmentservice.Entity.Department;
import com.naren.departmentservice.Exception.ConflictException;
import com.naren.departmentservice.Exception.ResourceNotFoundException;
import com.naren.departmentservice.Repository.DepartmentRepository;
import com.naren.departmentservice.Service.DepartmentService;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("departmentCrudService")
@Transactional
public class DepartmentCService implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentCService.class);

    private final DepartmentRepository repo;
    private final DepartmentMapper mapper;

    public DepartmentCService(DepartmentRepository repo, DepartmentMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        String name = sanitize(request.name());
        if (name == null) {
            throw new ValidationException("Department name is required");
        }

        if (repo.existsByName(name)) {
            throw new ConflictException("Department already exists with name: " + name);
        }

        Department department = Department.builder()
                .name(name)
                .description(sanitize(request.description()))
                .location(sanitize(request.location()))
                .managerId(request.managerId())
                .build();

        Department saved;
        try {
            saved = repo.save(department);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to save department: {}", e.getMessage());
            throw new ConflictException("Department name already taken");
        }
        return mapper.apply(saved);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentUpdateRequest request) {
        Department existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        applyNameUpdate(existing, request.name());
        if (request.description() != null) existing.setDescription(sanitize(request.description()));
        if (request.location() != null) existing.setLocation(sanitize(request.location()));
        if (request.managerId() != null) existing.setManagerId(request.managerId());

        Department saved;
        try {
            saved = repo.save(existing);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to update department {}: {}", id, e.getMessage());
            throw new ConflictException("Department name already taken");
        }
        return mapper.apply(saved);
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        throw new UnsupportedOperationException("Use departmentQueryService for query operations");
    }

    @Override
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
        throw new UnsupportedOperationException("Use departmentQueryService for query operations");
    }

    @Override
    public Page<DepartmentResponse> searchDepartments(String keyword, Pageable pageable) {
        throw new UnsupportedOperationException("Use departmentQueryService for query operations");
    }

    private void applyNameUpdate(Department existing, String inName) {
        String name = sanitize(inName);
        if (name == null || name.equalsIgnoreCase(existing.getName())) return;

        if (repo.existsByName(name)) {
            throw new ConflictException("Department name already taken: " + name);
        }
        existing.setName(name);
    }

    private String sanitize(String value) {
        if (value == null) return null;
        return value.trim().isEmpty() ? null : value.trim();
    }
}
