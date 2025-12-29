package com.attendance.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "attendance")
@CompoundIndexes({
    @CompoundIndex(name = "employee_date_idx", def = "{'employeeId': 1, 'date': 1}", unique = true)
})
public class Attendance {
    
    @Id
    private String id;
    
    private String employeeId;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    
    private AttendanceStatus status;
    
    private Double workingHours;
    
    @Builder.Default
    private boolean isLate = false;
    
    @Builder.Default
    private boolean isEarlyCheckout = false;
    
    private String remarks;
    
    @Builder.Default
    private boolean manualEntry = false;
    
    private String modifiedBy;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
