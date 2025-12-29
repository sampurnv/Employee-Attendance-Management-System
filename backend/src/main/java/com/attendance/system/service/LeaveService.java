package com.attendance.system.service;

import com.attendance.system.dto.LeaveRequest;
import com.attendance.system.dto.LeaveResponse;
import com.attendance.system.exception.BadRequestException;
import com.attendance.system.exception.ResourceNotFoundException;
import com.attendance.system.model.*;
import com.attendance.system.repository.EmployeeRepository;
import com.attendance.system.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveService {
    
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    
    @Transactional
    public LeaveResponse applyLeave(String employeeId, LeaveRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        // Validate dates
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }
        
        // Calculate number of days
        int numberOfDays = (int) ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        
        // Check leave balance
        int availableBalance = getLeaveBalance(employee, request.getLeaveType());
        if (availableBalance < numberOfDays) {
            throw new BadRequestException("Insufficient leave balance. Available: " + availableBalance + " days");
        }
        
        Leave leave = Leave.builder()
            .employeeId(employeeId)
            .leaveType(request.getLeaveType())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .numberOfDays(numberOfDays)
            .reason(request.getReason())
            .status(LeaveStatus.PENDING)
            .build();
        
        leave = leaveRepository.save(leave);
        log.info("Leave application submitted by employee: {}", employeeId);
        
        return mapToResponse(leave, employee);
    }
    
    @Transactional
    public LeaveResponse approveLeave(String leaveId, String approverId) {
        Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave", "id", leaveId));
        
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave is already " + leave.getStatus().name().toLowerCase());
        }
        
        Employee employee = employeeRepository.findById(leave.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", leave.getEmployeeId()));
        
        // Deduct leave balance
        deductLeaveBalance(employee, leave.getLeaveType(), leave.getNumberOfDays());
        
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedBy(approverId);
        leave.setApprovedAt(LocalDateTime.now());
        
        leave = leaveRepository.save(leave);
        log.info("Leave approved for employee: {} by: {}", leave.getEmployeeId(), approverId);
        
        return mapToResponse(leave, employee);
    }
    
    @Transactional
    public LeaveResponse rejectLeave(String leaveId, String approverId, String rejectionReason) {
        Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() -> new ResourceNotFoundException("Leave", "id", leaveId));
        
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave is already " + leave.getStatus().name().toLowerCase());
        }
        
        Employee employee = employeeRepository.findById(leave.getEmployeeId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", leave.getEmployeeId()));
        
        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApprovedBy(approverId);
        leave.setApprovedAt(LocalDateTime.now());
        leave.setRejectionReason(rejectionReason);
        
        leave = leaveRepository.save(leave);
        log.info("Leave rejected for employee: {} by: {}", leave.getEmployeeId(), approverId);
        
        return mapToResponse(leave, employee);
    }
    
    public List<LeaveResponse> getEmployeeLeaves(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        List<Leave> leaves = leaveRepository.findByEmployeeId(employeeId);
        return leaves.stream()
            .map(leave -> mapToResponse(leave, employee))
            .collect(Collectors.toList());
    }
    
    public List<LeaveResponse> getPendingLeaves() {
        List<Leave> leaves = leaveRepository.findByStatus(LeaveStatus.PENDING);
        return leaves.stream()
            .map(leave -> {
                Employee emp = employeeRepository.findById(leave.getEmployeeId()).orElse(null);
                return mapToResponse(leave, emp);
            })
            .collect(Collectors.toList());
    }
    
    public List<LeaveResponse> getAllLeaves() {
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream()
            .map(leave -> {
                Employee emp = employeeRepository.findById(leave.getEmployeeId()).orElse(null);
                return mapToResponse(leave, emp);
            })
            .collect(Collectors.toList());
    }
    
    private int getLeaveBalance(Employee employee, LeaveType leaveType) {
        switch (leaveType) {
            case CASUAL:
                return employee.getCasualLeaveBalance();
            case SICK:
                return employee.getSickLeaveBalance();
            case PAID:
                return employee.getPaidLeaveBalance();
            case UNPAID:
                return Integer.MAX_VALUE; // Unlimited unpaid leaves
            default:
                return 0;
        }
    }
    
    private void deductLeaveBalance(Employee employee, LeaveType leaveType, int days) {
        switch (leaveType) {
            case CASUAL:
                employee.setCasualLeaveBalance(employee.getCasualLeaveBalance() - days);
                break;
            case SICK:
                employee.setSickLeaveBalance(employee.getSickLeaveBalance() - days);
                break;
            case PAID:
                employee.setPaidLeaveBalance(employee.getPaidLeaveBalance() - days);
                break;
            case UNPAID:
                // No deduction for unpaid leaves
                break;
        }
        employeeRepository.save(employee);
    }
    
    private LeaveResponse mapToResponse(Leave leave, Employee employee) {
        String employeeName = null;
        String approverName = null;
        
        if (employee != null) {
            employeeName = employee.getFirstName() + " " + employee.getLastName();
        }
        
        if (leave.getApprovedBy() != null) {
            employeeRepository.findById(leave.getApprovedBy()).ifPresent(approver -> {
                // Set approver name in response
            });
        }
        
        return LeaveResponse.builder()
            .id(leave.getId())
            .employeeId(leave.getEmployeeId())
            .employeeName(employeeName)
            .leaveType(leave.getLeaveType())
            .startDate(leave.getStartDate())
            .endDate(leave.getEndDate())
            .numberOfDays(leave.getNumberOfDays())
            .reason(leave.getReason())
            .status(leave.getStatus())
            .approvedBy(leave.getApprovedBy())
            .approverName(approverName)
            .approvedAt(leave.getApprovedAt())
            .rejectionReason(leave.getRejectionReason())
            .createdAt(leave.getCreatedAt())
            .build();
    }
}
