package com.naren.leaveservice.Service.Impl;

import com.naren.leaveservice.DTO.*;
import com.naren.leaveservice.DTO.mapper.LeaveBalanceMapper;
import com.naren.leaveservice.Entity.LeaveBalance;
import com.naren.leaveservice.Entity.Enums.LeaveType;
import com.naren.leaveservice.Exception.BadRequestException;
import com.naren.leaveservice.Exception.ConflictException;
import com.naren.leaveservice.Exception.ResourceNotFoundException;
import com.naren.leaveservice.Repository.LeaveBalanceRepository;
import com.naren.leaveservice.Service.LeaveBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(LeaveBalanceServiceImpl.class);

    private final LeaveBalanceRepository repo;
    private final LeaveBalanceMapper mapper;

    public LeaveBalanceServiceImpl(LeaveBalanceRepository repo, LeaveBalanceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public LeaveBalanceResponse createLeaveBalance(LeaveBalanceCreateRequest request) {
        LeaveType leaveType = LeaveType.valueOf(request.leaveType().name());

        if (repo.findByEmployeeIdAndLeaveTypeAndYear(request.employeeId(), leaveType, request.year()).isPresent()) {
            throw new ConflictException("Leave balance already exists for this employee, type, and year");
        }

        LeaveBalance balance = LeaveBalance.builder()
                .employeeId(request.employeeId())
                .leaveType(leaveType)
                .totalDays(request.totalDays())
                .usedDays(0)
                .remainingDays(request.totalDays())
                .year(request.year())
                .build();

        LeaveBalance saved;
        try {
            saved = repo.save(balance);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to save leave balance: {}", e.getMessage());
            throw new ConflictException("Leave balance already exists for this employee, type, and year");
        }
        return mapper.apply(saved);
    }

    @Override
    @Transactional
    public LeaveBalanceResponse deductLeaveBalance(Long employeeId, String leaveType, int days, int year) {
        LeaveType type = LeaveType.valueOf(leaveType);
        LeaveBalance balance = repo.findByEmployeeIdAndLeaveTypeAndYear(employeeId, type, year)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

        if (balance.getRemainingDays() < days) {
            throw new BadRequestException("Insufficient leave balance. Remaining: " + balance.getRemainingDays());
        }

        balance.setUsedDays(balance.getUsedDays() + days);
        balance.setRemainingDays(balance.getTotalDays() - balance.getUsedDays());

        LeaveBalance saved = repo.save(balance);
        return mapper.apply(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveBalanceResponse getLeaveBalance(Long employeeId, String leaveType, int year) {
        LeaveType type = LeaveType.valueOf(leaveType);
        LeaveBalance balance = repo.findByEmployeeIdAndLeaveTypeAndYear(employeeId, type, year)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));
        return mapper.apply(balance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveBalanceResponse> getLeaveBalancesByEmployee(Long employeeId, int year) {
        return repo.findByEmployeeIdAndYear(employeeId, year)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
