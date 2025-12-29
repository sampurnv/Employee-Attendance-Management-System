package com.attendance.system.repository;

import com.attendance.system.model.Attendance;
import com.attendance.system.model.AttendanceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    
    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);
    
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);
    
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Attendance> findByEmployeeIdAndStatus(String employeeId, AttendanceStatus status);
    
    List<Attendance> findByDate(LocalDate date);
    
    boolean existsByEmployeeIdAndDate(String employeeId, LocalDate date);
    
    long countByEmployeeIdAndDateBetweenAndStatus(String employeeId, LocalDate startDate, LocalDate endDate, AttendanceStatus status);
}
