package com.naren.leaveservice.Controller;

import com.naren.leaveservice.DTO.LeaveRequestCreateRequest;
import com.naren.leaveservice.DTO.LeaveRequestResponse;
import com.naren.leaveservice.DTO.LeaveApprovalRequest;
import com.naren.leaveservice.Entity.Enums.LeaveStatus;
import com.naren.leaveservice.Entity.Enums.LeaveType;
import com.naren.leaveservice.Service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    @Qualifier("leaveRequestQueryService")
    private final LeaveRequestService leaveRequestQueryService;

    @Qualifier("leaveRequestCrudService")
    private final LeaveRequestService leaveRequestCrudService;

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> createLeaveRequest(@Valid @RequestBody LeaveRequestCreateRequest request) {
        LeaveRequestResponse response = leaveRequestCrudService.createLeaveRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestResponse> getLeaveRequestById(@PathVariable Long id) {
        LeaveRequestResponse response = leaveRequestQueryService.getLeaveRequestById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByEmployee(@PathVariable Long employeeId) {
        List<LeaveRequestResponse> response = leaveRequestQueryService.getLeaveRequestsByEmployee(employeeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByStatus(@PathVariable LeaveStatus status) {
        List<LeaveRequestResponse> response = leaveRequestQueryService.getLeaveRequestsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/type/{leaveType}")
    public ResponseEntity<List<LeaveRequestResponse>> getLeaveRequestsByEmployeeAndType(
            @PathVariable Long employeeId,
            @PathVariable LeaveType leaveType) {
        List<LeaveRequestResponse> response = leaveRequestQueryService.getLeaveRequestsByEmployeeAndType(employeeId, leaveType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestResponse> approveLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveApprovalRequest request) {
        LeaveRequestResponse response = leaveRequestCrudService.approveLeaveRequest(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelLeaveRequest(@PathVariable Long id) {
        leaveRequestCrudService.cancelLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
}
