package com.naren.departmentservice.Service;

import com.naren.departmentservice.DTO.DepartmentCreateRequest;
import com.naren.departmentservice.DTO.DepartmentResponse;
import com.naren.departmentservice.DTO.DepartmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentCreateRequest request);

    DepartmentResponse updateDepartment(Long id, DepartmentUpdateRequest request);

    void deleteDepartment(Long id);

    DepartmentResponse getDepartmentById(Long id);

    Page<DepartmentResponse> getAllDepartments(Pageable pageable);

    Page<DepartmentResponse> searchDepartments(String keyword, Pageable pageable);
}
