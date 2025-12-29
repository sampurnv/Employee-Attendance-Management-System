package com.attendance.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employees")
public class Employee {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String employeeCode;
    
    private String firstName;
    private String lastName;
    
    @Indexed(unique = true)
    private String email;
    
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    
    private String departmentId;
    private String designation;
    
    @Builder.Default
    private Role role = Role.EMPLOYEE;
    
    @Builder.Default
    private boolean active = true;
    
    private String profilePictureUrl;
    
    @Builder.Default
    private int casualLeaveBalance = 12;
    
    @Builder.Default
    private int sickLeaveBalance = 12;
    
    @Builder.Default
    private int paidLeaveBalance = 12;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private String userId;
}
