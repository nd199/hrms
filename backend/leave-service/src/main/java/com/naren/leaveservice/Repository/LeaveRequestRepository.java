package com.naren.leaveservice.Repository;

import com.naren.leaveservice.Entity.LeaveRequest;
import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import com.naren.leaveservice.Entity.Enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByEmployeeIdAndLeaveType(Long employeeId, LeaveType leaveType);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByEmployeeIdAndYear(Long employeeId, int year);
}
