package com.naren.leaveservice.DTO.mapper;

import com.naren.leaveservice.DTO.LeaveBalanceResponse;
import com.naren.leaveservice.Entity.LeaveBalance;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LeaveBalanceMapper implements Function<LeaveBalance, LeaveBalanceResponse> {

    @Override
    public LeaveBalanceResponse apply(LeaveBalance leaveBalance) {
        return new LeaveBalanceResponse(
                leaveBalance.getId(),
                leaveBalance.getEmployeeId(),
                leaveBalance.getLeaveType(),
                leaveBalance.getTotalDays(),
                leaveBalance.getUsedDays(),
                leaveBalance.getRemainingDays(),
                leaveBalance.getYear()
        );
    }
}
