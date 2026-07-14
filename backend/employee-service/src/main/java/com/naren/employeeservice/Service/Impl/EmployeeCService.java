package com.naren.employeeservice.Service.Impl;

import com.naren.employeeservice.DTO.*;
import com.naren.employeeservice.DTO.mapper.EmployeeMapper;
import com.naren.employeeservice.Entity.*;
import com.naren.employeeservice.Entity.Enums.EmploymentStatus;
import com.naren.employeeservice.Exception.ConflictException;
import com.naren.employeeservice.Exception.ResourceNotFoundException;
import com.naren.employeeservice.Repository.EmployeeRepository;
import com.naren.employeeservice.Service.EmployeeService;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("employeeCrudService")
@Transactional
public class EmployeeCService implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeCService.class);

    private final EmployeeRepository repo;
    private final EmployeeMapper mapper;

    public EmployeeCService(EmployeeRepository repo, EmployeeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        String email = sanitize(request.email());
        if (email == null) {
            throw new ValidationException("Email is required");
        }
        String employeeCode = sanitize(request.employeeCode());
        String firstName = sanitize(request.firstName());
        String lastName = sanitize(request.lastName());

        if (repo.existsByEmployeeCode(employeeCode)) {
            throw new ConflictException("Employee already exists with code: " + employeeCode);
        }
        if (repo.existsByEmail(email)) {
            throw new ConflictException("Employee already exists with email: " + email);
        }

        Employee employee = Employee.builder()
                .employeeCode(employeeCode)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(sanitize(request.phone()))
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .hireDate(request.hireDate())
                .employmentStatus(request.employmentStatus())
                .address(sanitize(request.address()))
                .city(sanitize(request.city()))
                .state(sanitize(request.state()))
                .country(sanitize(request.country()))
                .postalCode(sanitize(request.postalCode()))
                .emergencyContactName(sanitize(request.emergencyContactName()))
                .emergencyContactPhone(sanitize(request.emergencyContactPhone()))
                .departmentId(request.departmentId())
                .positionId(request.positionId())
                .managerId(request.managerId())
                .shiftId(request.shiftId())
                .build();

        Employee saved;
        try {
            saved = repo.save(employee);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to save employee: {}", e.getMessage());
            throw new ConflictException("Employee code or email already taken");
        }
        return mapper.apply(saved);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        applyCodeUpdate(existing, request.employeeCode());
        applyEmailUpdate(existing, request.email());
        applyNameUpdates(existing, request.firstName(), request.lastName());
        applyFieldUpdates(existing, request);

        Employee saved;
        try {
            saved = repo.save(existing);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to update employee {}: {}", id, e.getMessage());
            throw new ConflictException("Employee code or email already taken");
        }
        return mapper.apply(saved);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    @Override
    public EmployeeResponse getEmployeeByCode(String employeeCode) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(Long departmentId) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    @Override
    public List<EmployeeResponse> getEmployeesByStatus(EmploymentStatus status) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    @Override
    public List<EmployeeResponse> getEmployeesByManager(Long managerId) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String keyword, Pageable pageable) {
        throw new UnsupportedOperationException("Use employeeQueryService for query operations");
    }

    private void applyCodeUpdate(Employee existing, String inCode) {
        String code = sanitize(inCode);
        if (code == null || code.equalsIgnoreCase(existing.getEmployeeCode())) return;

        if (repo.existsByEmployeeCode(code)) {
            throw new ConflictException("Employee code already taken: " + code);
        }
        existing.setEmployeeCode(code);
    }

    private void applyEmailUpdate(Employee existing, String inEmail) {
        String email = sanitize(inEmail);
        if (email == null || email.equalsIgnoreCase(existing.getEmail())) return;

        if (repo.existsByEmail(email)) {
            throw new ConflictException("Email already taken: " + email);
        }
        existing.setEmail(email);
    }

    private void applyNameUpdates(Employee existing, String inFirst, String inLast) {
        String firstName = sanitize(inFirst);
        String lastName = sanitize(inLast);
        if (firstName != null) existing.setFirstName(firstName);
        if (lastName != null) existing.setLastName(lastName);
    }

    private void applyFieldUpdates(Employee existing, EmployeeUpdateRequest request) {
        if (request.phone() != null) existing.setPhone(sanitize(request.phone()));
        if (request.gender() != null) existing.setGender(request.gender());
        if (request.dateOfBirth() != null) existing.setDateOfBirth(request.dateOfBirth());
        if (request.hireDate() != null) existing.setHireDate(request.hireDate());
        if (request.employmentStatus() != null) existing.setEmploymentStatus(request.employmentStatus());
        if (request.address() != null) existing.setAddress(sanitize(request.address()));
        if (request.city() != null) existing.setCity(sanitize(request.city()));
        if (request.state() != null) existing.setState(sanitize(request.state()));
        if (request.country() != null) existing.setCountry(sanitize(request.country()));
        if (request.postalCode() != null) existing.setPostalCode(sanitize(request.postalCode()));
        if (request.emergencyContactName() != null) existing.setEmergencyContactName(sanitize(request.emergencyContactName()));
        if (request.emergencyContactPhone() != null) existing.setEmergencyContactPhone(sanitize(request.emergencyContactPhone()));

        if (request.departmentId() != null) existing.setDepartmentId(request.departmentId());
        if (request.positionId() != null) existing.setPositionId(request.positionId());
        if (request.managerId() != null) existing.setManagerId(request.managerId());
        if (request.shiftId() != null) existing.setShiftId(request.shiftId());
    }

    private String sanitize(String value) {
        if (value == null) return null;
        return value.trim().isEmpty() ? null : value.trim();
    }
}
