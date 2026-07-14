package com.naren.leaveservice.DTO;

import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import com.naren.leaveservice.Entity.Enums.LeaveType;

import java.time.LocalDateTime;

public record LeaveRequestResponse(
        Long id,
        Long employeeId,
        LeaveType leaveType,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String reason,
        LeaveStatus status,
        Long approvedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
