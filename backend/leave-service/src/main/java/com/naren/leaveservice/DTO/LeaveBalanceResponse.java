package com.naren.leaveservice.DTO;

import com.naren.leaveservice.Entity.Enums.LeaveType;

public record LeaveBalanceResponse(
        Long id,
        Long employeeId,
        LeaveType leaveType,
        int totalDays,
        int usedDays,
        int remainingDays,
        int year
) {
}
