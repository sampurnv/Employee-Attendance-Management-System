package com.attendance.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "leaves")
public class Leave {
    
    @Id
    private String id;
    
    private String employeeId;
    private LeaveType leaveType;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    private Integer numberOfDays;
    
    private String reason;
    
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;
    
    private String approvedBy;
    private LocalDateTime approvedAt;
    
    private String rejectionReason;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
