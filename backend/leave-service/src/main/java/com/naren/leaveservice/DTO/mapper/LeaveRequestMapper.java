package com.naren.leaveservice.DTO.mapper;

import com.naren.leaveservice.DTO.LeaveRequestResponse;
import com.naren.leaveservice.Entity.LeaveRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LeaveRequestMapper implements Function<LeaveRequest, LeaveRequestResponse> {

    @Override
    public LeaveRequestResponse apply(LeaveRequest leaveRequest) {
        return new LeaveRequestResponse(
                leaveRequest.getId(),
                leaveRequest.getEmployeeId(),
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus(),
                leaveRequest.getApprovedBy(),
                leaveRequest.getCreatedAt(),
                leaveRequest.getUpdatedAt()
        );
    }
}
