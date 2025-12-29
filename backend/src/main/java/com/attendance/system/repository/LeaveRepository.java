package com.attendance.system.repository;

import com.attendance.system.model.Leave;
import com.attendance.system.model.LeaveStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends MongoRepository<Leave, String> {
    
    List<Leave> findByEmployeeId(String employeeId);
    
    List<Leave> findByEmployeeIdAndStatus(String employeeId, LeaveStatus status);
    
    List<Leave> findByStatus(LeaveStatus status);
    
    List<Leave> findByEmployeeIdAndStartDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);
    
    List<Leave> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
