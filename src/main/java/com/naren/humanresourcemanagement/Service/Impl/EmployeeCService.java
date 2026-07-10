package com.naren.humanresourcemanagement.Service.Impl;

import com.naren.humanresourcemanagement.DTO.EmployeeCreateRequest;
import com.naren.humanresourcemanagement.DTO.EmployeeResponse;
import com.naren.humanresourcemanagement.DTO.EmployeeUpdateRequest;
import com.naren.humanresourcemanagement.DTO.mapper.EmployeeMapper;
import com.naren.humanresourcemanagement.Entity.Department;
import com.naren.humanresourcemanagement.Entity.Employee;
import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import com.naren.humanresourcemanagement.Entity.Position;
import com.naren.humanresourcemanagement.Entity.Shift;
import com.naren.humanresourcemanagement.Exception.ResourceNotFoundException;
import com.naren.humanresourcemanagement.Repository.EmployeeRepository;
import com.naren.humanresourcemanagement.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("employeeCrudService")
@RequiredArgsConstructor
@Transactional
public class EmployeeCService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Employee code already exists: " + request.getEmployeeCode());
        }
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }

        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .hireDate(request.getHireDate())
                .employmentStatus(request.getEmploymentStatus())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .build();

        if (request.getDepartmentId() != null) {
            Department department = new Department();
            department.setId(request.getDepartmentId());
            employee.setDepartment(department);
        }
        if (request.getPositionId() != null) {
            Position position = new Position();
            position.setId(request.getPositionId());
            employee.setPosition(position);
        }
        if (request.getManagerId() != null) {
            Employee manager = new Employee();
            manager.setId(request.getManagerId());
            employee.setManager(manager);
        }
        if (request.getShiftId() != null) {
            Shift shift = new Shift();
            shift.setId(request.getShiftId());
            employee.setShift(shift);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.apply(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (request.getEmployeeCode() != null && !existingEmployee.getEmployeeCode().equals(request.getEmployeeCode())) {
            if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
                throw new IllegalArgumentException("Employee code already exists: " + request.getEmployeeCode());
            }
        }
        if (request.getEmail() != null && !existingEmployee.getEmail().equals(request.getEmail())) {
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + request.getEmail());
            }
        }

        if (request.getEmployeeCode() != null) existingEmployee.setEmployeeCode(request.getEmployeeCode());
        if (request.getFirstName() != null) existingEmployee.setFirstName(request.getFirstName());
        if (request.getLastName() != null) existingEmployee.setLastName(request.getLastName());
        if (request.getEmail() != null) existingEmployee.setEmail(request.getEmail());
        if (request.getPhone() != null) existingEmployee.setPhone(request.getPhone());
        if (request.getGender() != null) existingEmployee.setGender(request.getGender());
        if (request.getDateOfBirth() != null) existingEmployee.setDateOfBirth(request.getDateOfBirth());
        if (request.getHireDate() != null) existingEmployee.setHireDate(request.getHireDate());
        if (request.getEmploymentStatus() != null) existingEmployee.setEmploymentStatus(request.getEmploymentStatus());
        if (request.getAddress() != null) existingEmployee.setAddress(request.getAddress());
        if (request.getCity() != null) existingEmployee.setCity(request.getCity());
        if (request.getState() != null) existingEmployee.setState(request.getState());
        if (request.getCountry() != null) existingEmployee.setCountry(request.getCountry());
        if (request.getPostalCode() != null) existingEmployee.setPostalCode(request.getPostalCode());
        if (request.getEmergencyContactName() != null)
            existingEmployee.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null)
            existingEmployee.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getDepartmentId() != null) {
            Department department = new Department();
            department.setId(request.getDepartmentId());
            existingEmployee.setDepartment(department);
        }
        if (request.getPositionId() != null) {
            Position position = new Position();
            position.setId(request.getPositionId());
            existingEmployee.setPosition(position);
        }
        if (request.getManagerId() != null) {
            Employee manager = new Employee();
            manager.setId(request.getManagerId());
            existingEmployee.setManager(manager);
        }
        if (request.getShiftId() != null) {
            Shift shift = new Shift();
            shift.setId(request.getShiftId());
            existingEmployee.setShift(shift);
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.apply(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
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
}
