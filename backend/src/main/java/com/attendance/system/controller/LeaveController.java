package com.attendance.system.controller;

import com.attendance.system.dto.LeaveRequest;
import com.attendance.system.dto.LeaveResponse;
import com.attendance.system.security.UserDetailsImpl;
import com.attendance.system.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Leave Management", description = "Leave management APIs")
public class LeaveController {
    
    private final LeaveService leaveService;
    
    @PostMapping
    @Operation(summary = "Apply for leave")
    public ResponseEntity<LeaveResponse> applyLeave(
            @Valid @RequestBody LeaveRequest request,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String employeeId = userDetails.getId(); // Simplified
        
        return new ResponseEntity<>(leaveService.applyLeave(employeeId, request), HttpStatus.CREATED);
    }
    
    @PutMapping("/{leaveId}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Approve leave")
    public ResponseEntity<LeaveResponse> approveLeave(
            @PathVariable String leaveId,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String approverId = userDetails.getId();
        
        return ResponseEntity.ok(leaveService.approveLeave(leaveId, approverId));
    }
    
    @PutMapping("/{leaveId}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Reject leave")
    public ResponseEntity<LeaveResponse> rejectLeave(
            @PathVariable String leaveId,
            @RequestParam String rejectionReason,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String approverId = userDetails.getId();
        
        return ResponseEntity.ok(leaveService.rejectLeave(leaveId, approverId, rejectionReason));
    }
    
    @GetMapping("/my-leaves")
    @Operation(summary = "Get my leave applications")
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String employeeId = userDetails.getId(); // Simplified
        
        return ResponseEntity.ok(leaveService.getEmployeeLeaves(employeeId));
    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get employee leave applications")
    public ResponseEntity<List<LeaveResponse>> getEmployeeLeaves(@PathVariable String employeeId) {
        return ResponseEntity.ok(leaveService.getEmployeeLeaves(employeeId));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get pending leave applications")
    public ResponseEntity<List<LeaveResponse>> getPendingLeaves() {
        return ResponseEntity.ok(leaveService.getPendingLeaves());
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all leave applications")
    public ResponseEntity<List<LeaveResponse>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }
}
