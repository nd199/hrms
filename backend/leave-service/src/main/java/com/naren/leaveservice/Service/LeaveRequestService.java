package com.naren.leaveservice.Service;

import com.naren.leaveservice.DTO.LeaveRequestCreateRequest;
import com.naren.leaveservice.DTO.LeaveRequestResponse;
import com.naren.leaveservice.DTO.LeaveApprovalRequest;
import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import com.naren.leaveservice.Entity.Enums.LeaveType;

import java.util.List;

public interface LeaveRequestService {

    LeaveRequestResponse createLeaveRequest(LeaveRequestCreateRequest request);

    LeaveRequestResponse approveLeaveRequest(Long id, LeaveApprovalRequest request);

    void cancelLeaveRequest(Long id);

    LeaveRequestResponse getLeaveRequestById(Long id);

    List<LeaveRequestResponse> getLeaveRequestsByEmployee(Long employeeId);

    List<LeaveRequestResponse> getLeaveRequestsByStatus(LeaveStatus status);

    List<LeaveRequestResponse> getLeaveRequestsByEmployeeAndType(Long employeeId, LeaveType leaveType);
}
