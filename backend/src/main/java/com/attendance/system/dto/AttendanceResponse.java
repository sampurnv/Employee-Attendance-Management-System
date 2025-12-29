package com.attendance.system.dto;

import com.attendance.system.model.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    
    private String id;
    private String employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;
    private Double workingHours;
    private boolean isLate;
    private boolean isEarlyCheckout;
    private String remarks;
    private boolean manualEntry;
    private LocalDateTime createdAt;
}
