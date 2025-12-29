package com.attendance.system.dto;

import com.attendance.system.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    
    private String id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String departmentId;
    private String departmentName;
    private String designation;
    private Role role;
    private boolean active;
    private String profilePictureUrl;
    private int casualLeaveBalance;
    private int sickLeaveBalance;
    private int paidLeaveBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
