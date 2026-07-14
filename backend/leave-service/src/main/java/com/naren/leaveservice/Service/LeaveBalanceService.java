package com.naren.leaveservice.Service;

import com.naren.leaveservice.DTO.LeaveBalanceCreateRequest;
import com.naren.leaveservice.DTO.LeaveBalanceResponse;

import java.util.List;

public interface LeaveBalanceService {

    LeaveBalanceResponse createLeaveBalance(LeaveBalanceCreateRequest request);

    LeaveBalanceResponse deductLeaveBalance(Long employeeId, String leaveType, int days, int year);

    LeaveBalanceResponse getLeaveBalance(Long employeeId, String leaveType, int year);

    List<LeaveBalanceResponse> getLeaveBalancesByEmployee(Long employeeId, int year);
}
