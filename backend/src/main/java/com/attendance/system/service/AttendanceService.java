package com.attendance.system.service;

import com.attendance.system.dto.AttendanceResponse;
import com.attendance.system.dto.CheckInRequest;
import com.attendance.system.dto.CheckOutRequest;
import com.attendance.system.exception.BadRequestException;
import com.attendance.system.exception.ResourceNotFoundException;
import com.attendance.system.model.Attendance;
import com.attendance.system.model.AttendanceStatus;
import com.attendance.system.model.Employee;
import com.attendance.system.repository.AttendanceRepository;
import com.attendance.system.repository.EmployeeRepository;
import com.attendance.system.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final HolidayRepository holidayRepository;
    
    @Value("${app.office.start-time}")
    private String officeStartTime;
    
    @Value("${app.office.late-threshold}")
    private int lateThresholdMinutes;
    
    @Transactional
    public AttendanceResponse checkIn(String employeeId, CheckInRequest request) {
        // Verify employee exists
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        LocalDate today = LocalDate.now();
        
        // Check if already checked in today
        if (attendanceRepository.existsByEmployeeIdAndDate(employeeId, today)) {
            throw new BadRequestException("Already checked in today");
        }
        
        // Check if today is a holiday
        if (holidayRepository.existsByDate(today)) {
            throw new BadRequestException("Cannot check in on a holiday");
        }
        
        LocalTime checkInTime = request.getCheckInTime() != null ? 
            request.getCheckInTime() : LocalTime.now();
        
        // Check if late
        LocalTime startTime = LocalTime.parse(officeStartTime);
        boolean isLate = checkInTime.isAfter(startTime.plusMinutes(lateThresholdMinutes));
        
        Attendance attendance = Attendance.builder()
            .employeeId(employeeId)
            .date(today)
            .checkInTime(checkInTime)
            .status(AttendanceStatus.PRESENT)
            .isLate(isLate)
            .remarks(request.getRemarks())
            .manualEntry(false)
            .build();
        
        attendance = attendanceRepository.save(attendance);
        log.info("Check-in recorded for employee: {} at {}", employeeId, checkInTime);
        
        return mapToResponse(attendance, employee);
    }
    
    @Transactional
    public AttendanceResponse checkOut(String employeeId, CheckOutRequest request) {
        LocalDate today = LocalDate.now();
        
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
            .orElseThrow(() -> new BadRequestException("No check-in found for today"));
        
        if (attendance.getCheckOutTime() != null) {
            throw new BadRequestException("Already checked out today");
        }
        
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        LocalTime checkOutTime = request.getCheckOutTime() != null ? 
            request.getCheckOutTime() : LocalTime.now();
        
        // Calculate working hours
        Duration duration = Duration.between(attendance.getCheckInTime(), checkOutTime);
        double workingHours = duration.toMinutes() / 60.0;
        
        attendance.setCheckOutTime(checkOutTime);
        attendance.setWorkingHours(workingHours);
        
        // Determine if half-day (less than 4 hours)
        if (workingHours < 4) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        }
        
        if (request.getRemarks() != null) {
            String updatedRemarks = attendance.getRemarks() != null ? 
                attendance.getRemarks() + "; " + request.getRemarks() : request.getRemarks();
            attendance.setRemarks(updatedRemarks);
        }
        
        attendance = attendanceRepository.save(attendance);
        log.info("Check-out recorded for employee: {} at {}", employeeId, checkOutTime);
        
        return mapToResponse(attendance, employee);
    }
    
    public AttendanceResponse getTodayAttendance(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, today)
            .orElse(null);
        
        if (attendance == null) {
            return null;
        }
        
        return mapToResponse(attendance, employee);
    }
    
    public List<AttendanceResponse> getEmployeeAttendance(String employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        List<Attendance> attendances = attendanceRepository.findByEmployeeIdAndDateBetween(
            employeeId, startDate, endDate);
        
        return attendances.stream()
            .map(att -> mapToResponse(att, employee))
            .collect(Collectors.toList());
    }
    
    public List<AttendanceResponse> getAllAttendance(LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendances = attendanceRepository.findByDateBetween(startDate, endDate);
        
        return attendances.stream()
            .map(att -> {
                Employee emp = employeeRepository.findById(att.getEmployeeId())
                    .orElse(null);
                return mapToResponse(att, emp);
            })
            .collect(Collectors.toList());
    }
    
    @Transactional
    public AttendanceResponse manualAttendanceEntry(String employeeId, LocalDate date, 
                                                     LocalTime checkInTime, LocalTime checkOutTime,
                                                     AttendanceStatus status, String remarks, 
                                                     String modifiedBy) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        
        // Check if attendance already exists
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, date)
            .orElse(null);
        
        if (attendance == null) {
            attendance = Attendance.builder()
                .employeeId(employeeId)
                .date(date)
                .build();
        }
        
        attendance.setCheckInTime(checkInTime);
        attendance.setCheckOutTime(checkOutTime);
        attendance.setStatus(status);
        attendance.setRemarks(remarks);
        attendance.setManualEntry(true);
        attendance.setModifiedBy(modifiedBy);
        
        if (checkInTime != null && checkOutTime != null) {
            Duration duration = Duration.between(checkInTime, checkOutTime);
            attendance.setWorkingHours(duration.toMinutes() / 60.0);
        }
        
        attendance = attendanceRepository.save(attendance);
        log.info("Manual attendance entry for employee: {} on {}", employeeId, date);
        
        return mapToResponse(attendance, employee);
    }
    
    private AttendanceResponse mapToResponse(Attendance attendance, Employee employee) {
        String employeeName = null;
        if (employee != null) {
            employeeName = employee.getFirstName() + " " + employee.getLastName();
        }
        
        return AttendanceResponse.builder()
            .id(attendance.getId())
            .employeeId(attendance.getEmployeeId())
            .employeeName(employeeName)
            .date(attendance.getDate())
            .checkInTime(attendance.getCheckInTime())
            .checkOutTime(attendance.getCheckOutTime())
            .status(attendance.getStatus())
            .workingHours(attendance.getWorkingHours())
            .isLate(attendance.isLate())
            .isEarlyCheckout(attendance.isEarlyCheckout())
            .remarks(attendance.getRemarks())
            .manualEntry(attendance.isManualEntry())
            .createdAt(attendance.getCreatedAt())
            .build();
    }
}
