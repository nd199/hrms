package com.naren.departmentservice.Controller;

import com.naren.departmentservice.DTO.DepartmentCreateRequest;
import com.naren.departmentservice.DTO.DepartmentResponse;
import com.naren.departmentservice.DTO.DepartmentUpdateRequest;
import com.naren.departmentservice.Service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    @Qualifier("departmentQueryService")
    private final DepartmentService departmentQueryService;

    @Qualifier("departmentCrudService")
    private final DepartmentService departmentCrudService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        DepartmentResponse response = departmentCrudService.createDepartment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        DepartmentResponse response = departmentQueryService.getDepartmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartments(Pageable pageable) {
        Page<DepartmentResponse> response = departmentQueryService.getAllDepartments(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DepartmentResponse>> searchDepartments(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<DepartmentResponse> response = departmentQueryService.searchDepartments(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentUpdateRequest request) {
        DepartmentResponse response = departmentCrudService.updateDepartment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentCrudService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
