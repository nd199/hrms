package com.naren.leaveservice.DTO;

import com.naren.leaveservice.Entity.Enums.LeaveType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LeaveRequestCreateRequest(
        @NotNull(message = "Employee ID is required")
        Long employeeId,

        @NotNull(message = "Leave type is required")
        LeaveType leaveType,

        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @NotNull(message = "End date is required")
        LocalDateTime endDate,

        @Size(max = 500, message = "Reason must not exceed 500 characters")
        String reason
) {
}
