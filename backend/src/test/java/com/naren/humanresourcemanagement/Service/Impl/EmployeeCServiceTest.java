package com.naren.humanresourcemanagement.Service.Impl;

import com.naren.humanresourcemanagement.DTO.EmployeeCreateRequest;
import com.naren.humanresourcemanagement.DTO.EmployeeResponse;
import com.naren.humanresourcemanagement.DTO.mapper.EmployeeMapper;
import com.naren.humanresourcemanagement.Entity.Employee;
import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import com.naren.humanresourcemanagement.Entity.Enums.Gender;
import com.naren.humanresourcemanagement.Exception.ConflictException;
import com.naren.humanresourcemanagement.Repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeCServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeCService underTest;

    @Test
    void createEmployee() {
        EmployeeCreateRequest req = new EmployeeCreateRequest(
                "EMP001",
                "Naren",
                "Kumar",
                "naren@test.com",
                "1234567890",
                Gender.MALE,
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0),
                EmploymentStatus.ACTIVE,
                "123 Main St",
                "Chennai",
                "Tamil Nadu",
                "India",
                "600001",
                "Emergency Contact",
                "9876543210",
                1L,
                null,
                null,
                null
        );

        when(repository.existsByEmployeeCode(req.employeeCode())).thenReturn(false);
        when(repository.existsByEmail(req.email())).thenReturn(false);

        when(repository.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee emp = invocation.getArgument(0);
            emp.setId(1L);
            return emp;
        });

        EmployeeResponse response = new EmployeeResponse(
                1L,
                req.employeeCode(),
                req.firstName(),
                req.lastName(),
                req.email(),
                req.phone(),
                req.gender(),
                req.dateOfBirth(),
                req.hireDate(),
                req.employmentStatus(),
                req.address(),
                req.city(),
                req.state(),
                req.country(),
                req.postalCode(),
                req.emergencyContactName(),
                req.emergencyContactPhone(),
                req.departmentId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(mapper.apply(any(Employee.class))).thenReturn(response);

        EmployeeResponse result = underTest.createEmployee(req);

        assertThat(result).isNotNull();
        assertThat(result.employeeCode()).isEqualTo("EMP001");
        assertThat(result.email()).isEqualTo("naren@test.com");
        assertThat(result.firstName()).isEqualTo("Naren");
        assertThat(result.departmentId()).isEqualTo(1L);

        verify(repository).existsByEmployeeCode("EMP001");
        verify(repository).existsByEmail("naren@test.com");
        verify(repository).save(any(Employee.class));
        verify(mapper).apply(any(Employee.class));
    }

    @Test
    void createEmployee_duplicateEmployeeCode_throws() {
        EmployeeCreateRequest req = new EmployeeCreateRequest(
                "EMP001", "Naren", "Kumar", "naren@test.com",
                "1234567890", Gender.MALE,
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0),
                EmploymentStatus.ACTIVE,
                null, null, null, null, null,
                null, null, 1L, null, null, null
        );

        when(repository.existsByEmployeeCode("EMP001")).thenReturn(true);

        assertThatThrownBy(() -> underTest.createEmployee(req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("EMP001");

        verify(repository, never()).save(any());
    }

    @Test
    void createEmployee_duplicateEmail_throws() {
        EmployeeCreateRequest req = new EmployeeCreateRequest(
                "EMP001", "Naren", "Kumar", "naren@test.com",
                "1234567890", Gender.MALE,
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 0, 0),
                EmploymentStatus.ACTIVE,
                null, null, null, null, null,
                null, null, 1L, null, null, null
        );

        when(repository.existsByEmployeeCode("EMP001")).thenReturn(false);
        when(repository.existsByEmail("naren@test.com")).thenReturn(true);

        assertThatThrownBy(() -> underTest.createEmployee(req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("naren@test.com");

        verify(repository, never()).save(any());
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }
}
