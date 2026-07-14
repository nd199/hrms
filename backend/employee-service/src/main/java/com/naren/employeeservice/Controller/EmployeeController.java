package com.naren.employeeservice.Controller;

import com.naren.employeeservice.DTO.EmployeeCreateRequest;
import com.naren.employeeservice.DTO.EmployeeResponse;
import com.naren.employeeservice.DTO.EmployeeUpdateRequest;
import com.naren.employeeservice.Entity.Enums.EmploymentStatus;
import com.naren.employeeservice.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Qualifier("employeeQueryService")
    private final EmployeeService employeeQueryService;

    @Qualifier("employeeCrudService")
    private final EmployeeService employeeCrudService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
        EmployeeResponse response = employeeCrudService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeQueryService.getEmployeeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<EmployeeResponse> getEmployeeByCode(@PathVariable String employeeCode) {
        EmployeeResponse response = employeeQueryService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(Pageable pageable) {
        Page<EmployeeResponse> response = employeeQueryService.getAllEmployees(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        List<EmployeeResponse> response = employeeQueryService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByStatus(@PathVariable EmploymentStatus status) {
        List<EmployeeResponse> response = employeeQueryService.getEmployeesByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByManager(@PathVariable Long managerId) {
        List<EmployeeResponse> response = employeeQueryService.getEmployeesByManager(managerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<EmployeeResponse> response = employeeQueryService.searchEmployees(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        EmployeeResponse response = employeeCrudService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeCrudService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
