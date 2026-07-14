package com.naren.leaveservice.Service.Impl;

import com.naren.leaveservice.DTO.*;
import com.naren.leaveservice.DTO.mapper.LeaveRequestMapper;
import com.naren.leaveservice.Entity.LeaveRequest;
import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import com.naren.leaveservice.Entity.Enums.LeaveType;
import com.naren.leaveservice.Exception.ResourceNotFoundException;
import com.naren.leaveservice.Repository.LeaveRequestRepository;
import com.naren.leaveservice.Service.LeaveRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("leaveRequestQueryService")
@Transactional(readOnly = true)
public class LeaveRequestQService implements LeaveRequestService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestQService.class);

    private final LeaveRequestRepository repo;
    private final LeaveRequestMapper mapper;

    public LeaveRequestQService(LeaveRequestRepository repo, LeaveRequestMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public LeaveRequestResponse getLeaveRequestById(Long id) {
        LeaveRequest leaveRequest = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
        return mapper.apply(leaveRequest);
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByEmployee(Long employeeId) {
        return repo.findByEmployeeId(employeeId)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByStatus(LeaveStatus status) {
        return repo.findByStatus(status)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getLeaveRequestsByEmployeeAndType(Long employeeId, LeaveType leaveType) {
        return repo.findByEmployeeIdAndLeaveType(employeeId, leaveType)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveRequestResponse createLeaveRequest(LeaveRequestCreateRequest request) {
        throw new UnsupportedOperationException("Use leaveRequestCrudService for create operations");
    }

    @Override
    public LeaveRequestResponse approveLeaveRequest(Long id, LeaveApprovalRequest request) {
        throw new UnsupportedOperationException("Use leaveRequestCrudService for approve operations");
    }

    @Override
    public void cancelLeaveRequest(Long id) {
        throw new UnsupportedOperationException("Use leaveRequestCrudService for cancel operations");
    }
}
