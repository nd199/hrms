package com.naren.leaveservice.DTO;

import com.naren.leaveservice.Entity.Enums.LeaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LeaveBalanceCreateRequest(
        @NotNull(message = "Employee ID is required")
        Long employeeId,

        @NotNull(message = "Leave type is required")
        LeaveType leaveType,

        @Min(value = 1, message = "Total days must be at least 1")
        int totalDays,

        @NotNull(message = "Year is required")
        int year
) {
}
