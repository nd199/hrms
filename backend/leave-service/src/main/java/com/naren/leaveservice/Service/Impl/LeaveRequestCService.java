package com.naren.leaveservice.Service.Impl;

import com.naren.leaveservice.DTO.*;
import com.naren.leaveservice.DTO.mapper.LeaveRequestMapper;
import com.naren.leaveservice.Entity.LeaveRequest;
import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import com.naren.leaveservice.Entity.Enums.LeaveType;
import com.naren.leaveservice.Exception.BadRequestException;
import com.naren.leaveservice.Exception.ConflictException;
import com.naren.leaveservice.Exception.ResourceNotFoundException;
import com.naren.leaveservice.Repository.LeaveRequestRepository;
import com.naren.leaveservice.Service.LeaveRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service("leaveRequestCrudService")
@Transactional
public class LeaveRequestCService implements LeaveRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestCService.class);

    private final LeaveRequestRepository repo;
    private final LeaveRequestMapper mapper;

    public LeaveRequestCService(LeaveRequestRepository repo, LeaveRequestMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public LeaveRequestResponse createLeaveRequest(LeaveRequestCreateRequest request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        long days = ChronoUnit.DAYS.between(request.startDate().toLocalDate(), request.endDate().toLocalDate()) + 1;
        if (days > 30) {
            throw new BadRequestException("Leave request cannot exceed 30 days");
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employeeId(request.employeeId())
                .leaveType(request.leaveType())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reason(request.reason())
                .status(LeaveStatus.PENDING)
                .build();

        LeaveRequest saved = repo.save(leaveRequest);
        return mapper.apply(saved);
    }

    @Override
    @Transactional
    public LeaveRequestResponse approveLeaveRequest(Long id, LeaveApprovalRequest request) {
        LeaveRequest existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));

        if (existing.getStatus() != LeaveStatus.PENDING) {
            throw new ConflictException("Leave request is not pending");
        }

        existing.setStatus(request.status());
        existing.setApprovedBy(request.approvedBy());

        LeaveRequest saved = repo.save(existing);
        return mapper.apply(saved);
    }

    @Override
    public void cancelLeaveRequest(Long id) {
        LeaveRequest existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));

        if (existing.getStatus() != LeaveStatus.PENDING) {
            throw new ConflictException("Only pending leave requests can be cancelled");
        }

        existing.setStatus(LeaveStatus.CANCELLED);
        repo.save(existing);
    }

    @Override
    public LeaveRequestResponse getLeaveRequestById(Long id) {
        throw new UnsupportedOperationException("Use leaveRequestQueryService for query operations");
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByEmployee(Long employeeId) {
        throw new UnsupportedOperationException("Use leaveRequestQueryService for query operations");
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByStatus(LeaveStatus status) {
        throw new UnsupportedOperationException("Use leaveRequestQueryService for query operations");
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByEmployeeAndType(Long employeeId, LeaveType leaveType) {
        throw new UnsupportedOperationException("Use leaveRequestQueryService for query operations");
    }
}
