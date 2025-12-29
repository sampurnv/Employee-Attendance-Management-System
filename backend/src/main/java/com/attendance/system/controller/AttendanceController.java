package com.attendance.system.controller;

import com.attendance.system.dto.AttendanceResponse;
import com.attendance.system.dto.CheckInRequest;
import com.attendance.system.dto.CheckOutRequest;
import com.attendance.system.model.AttendanceStatus;
import com.attendance.system.security.UserDetailsImpl;
import com.attendance.system.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attendance Management", description = "Attendance management APIs")
public class AttendanceController {
    
    private final AttendanceService attendanceService;
    
    @PostMapping("/check-in")
    @Operation(summary = "Check in for the day")
    public ResponseEntity<AttendanceResponse> checkIn(
            @RequestBody(required = false) CheckInRequest request,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // In a real scenario, get employeeId from user details
        String employeeId = userDetails.getId(); // Simplified
        
        if (request == null) {
            request = new CheckInRequest();
        }
        
        return new ResponseEntity<>(attendanceService.checkIn(employeeId, request), HttpStatus.CREATED);
    }
    
    @PostMapping("/check-out")
    @Operation(summary = "Check out for the day")
    public ResponseEntity<AttendanceResponse> checkOut(
            @RequestBody(required = false) CheckOutRequest request,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String employeeId = userDetails.getId(); // Simplified
        
        if (request == null) {
            request = new CheckOutRequest();
        }
        
        return ResponseEntity.ok(attendanceService.checkOut(employeeId, request));
    }
    
    @GetMapping("/today")
    @Operation(summary = "Get today's attendance")
    public ResponseEntity<AttendanceResponse> getTodayAttendance(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String employeeId = userDetails.getId(); // Simplified
        
        AttendanceResponse response = attendanceService.getTodayAttendance(employeeId);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get employee attendance history")
    public ResponseEntity<List<AttendanceResponse>> getEmployeeAttendance(
            @PathVariable String employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return ResponseEntity.ok(attendanceService.getEmployeeAttendance(employeeId, startDate, endDate));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all attendance records")
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        return ResponseEntity.ok(attendanceService.getAllAttendance(startDate, endDate));
    }
    
    @PostMapping("/manual")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Manual attendance entry")
    public ResponseEntity<AttendanceResponse> manualAttendanceEntry(
            @RequestParam String employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkInTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOutTime,
            @RequestParam AttendanceStatus status,
            @RequestParam(required = false) String remarks,
            Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String modifiedBy = userDetails.getId();
        
        return new ResponseEntity<>(
            attendanceService.manualAttendanceEntry(employeeId, date, checkInTime, checkOutTime, 
                                                    status, remarks, modifiedBy),
            HttpStatus.CREATED
        );
    }
}
