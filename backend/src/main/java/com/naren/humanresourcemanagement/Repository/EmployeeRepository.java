package com.naren.humanresourcemanagement.Repository;

import com.naren.humanresourcemanagement.Entity.Employee;
import com.naren.humanresourcemanagement.Entity.Enums.EmploymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByPositionId(Long positionId);

    List<Employee> findByManagerId(Long managerId);

    List<Employee> findByEmploymentStatus(EmploymentStatus status);

    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND e.employmentStatus = :status")
    List<Employee> findByDepartmentIdAndStatus(
            @Param("departmentId") Long departmentId,
            @Param("status") EmploymentStatus status);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) " +
            "LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.employmentStatus = :status")
    List<Employee> findActiveEmployees(@Param("status") EmploymentStatus status);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :dept_id")
    Page<Employee> findEmployeesByDepartment(@Param("dept_id") String dept_id, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.firstName) " +
            "LIKE LOWER(CONCAT(:name, '%'))")
    Page<Employee> findEmployeeByNameStartingWith(@Param("name") String name, Pageable pageable);
}
