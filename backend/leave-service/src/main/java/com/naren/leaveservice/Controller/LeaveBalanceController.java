package com.naren.leaveservice.Controller;

import com.naren.leaveservice.DTO.LeaveBalanceCreateRequest;
import com.naren.leaveservice.DTO.LeaveBalanceResponse;
import com.naren.leaveservice.Service.LeaveBalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
@RequiredArgsConstructor
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    @PostMapping
    public ResponseEntity<LeaveBalanceResponse> createLeaveBalance(@Valid @RequestBody LeaveBalanceCreateRequest request) {
        LeaveBalanceResponse response = leaveBalanceService.createLeaveBalance(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}/type/{leaveType}/year/{year}")
    public ResponseEntity<LeaveBalanceResponse> getLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable String leaveType,
            @PathVariable int year) {
        LeaveBalanceResponse response = leaveBalanceService.getLeaveBalance(employeeId, leaveType, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/year/{year}")
    public ResponseEntity<List<LeaveBalanceResponse>> getLeaveBalancesByEmployee(
            @PathVariable Long employeeId,
            @PathVariable int year) {
        List<LeaveBalanceResponse> response = leaveBalanceService.getLeaveBalancesByEmployee(employeeId, year);
        return ResponseEntity.ok(response);
    }
}
