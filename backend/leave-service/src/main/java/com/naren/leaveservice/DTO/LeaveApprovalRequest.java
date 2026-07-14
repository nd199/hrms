package com.naren.leaveservice.DTO;

import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;

public record LeaveApprovalRequest(
        @NotNull(message = "Status is required")
        LeaveStatus status,

        @NotNull(message = "Approver ID is required")
        Long approvedBy
) {
}
