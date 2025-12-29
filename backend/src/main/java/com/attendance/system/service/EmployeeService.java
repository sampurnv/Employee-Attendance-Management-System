package com.attendance.system.service;

import com.attendance.system.dto.EmployeeRequest;
import com.attendance.system.dto.EmployeeResponse;
import com.attendance.system.exception.DuplicateResourceException;
import com.attendance.system.exception.ResourceNotFoundException;
import com.attendance.system.model.Employee;
import com.attendance.system.model.User;
import com.attendance.system.repository.DepartmentRepository;
import com.attendance.system.repository.EmployeeRepository;
import com.attendance.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already in use");
        }
        
        // Generate unique employee code
        String employeeCode = generateEmployeeCode();
        
        // Create User account for employee
        User user = User.builder()
            .username(request.getEmail().split("@")[0])
            .email(request.getEmail())
            .password(passwordEncoder.encode("Welcome@123")) // Default password
            .roles(Set.of(request.getRole()))
            .active(true)
            .build();
        
        user = userRepository.save(user);
        
        // Create Employee
        Employee employee = Employee.builder()
            .employeeCode(employeeCode)
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .phone(request.getPhone())
            .address(request.getAddress())
            .dateOfBirth(request.getDateOfBirth())
            .joiningDate(request.getJoiningDate())
            .departmentId(request.getDepartmentId())
            .designation(request.getDesignation())
            .role(request.getRole())
            .active(true)
            .userId(user.getId())
            .casualLeaveBalance(request.getCasualLeaveBalance() != null ? request.getCasualLeaveBalance() : 12)
            .sickLeaveBalance(request.getSickLeaveBalance() != null ? request.getSickLeaveBalance() : 12)
            .paidLeaveBalance(request.getPaidLeaveBalance() != null ? request.getPaidLeaveBalance() : 12)
            .build();
        
        employee = employeeRepository.save(employee);
        
        // Update user with employee ID
        user.setEmployeeId(employee.getId());
        userRepository.save(user);
        
        log.info("Employee created: {}", employee.getEmployeeCode());
        
        return mapToResponse(employee);
    }
    
    public EmployeeResponse getEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        return mapToResponse(employee);
    }
    
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<EmployeeResponse> getActiveEmployees() {
        return employeeRepository.findByActive(true).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public List<EmployeeResponse> getEmployeesByDepartment(String departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public EmployeeResponse updateEmployee(String id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        // Check email uniqueness if changed
        if (!employee.getEmail().equals(request.getEmail()) && 
            employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already in use");
        }
        
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAddress(request.getAddress());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setDesignation(request.getDesignation());
        employee.setRole(request.getRole());
        
        if (request.getCasualLeaveBalance() != null) {
            employee.setCasualLeaveBalance(request.getCasualLeaveBalance());
        }
        if (request.getSickLeaveBalance() != null) {
            employee.setSickLeaveBalance(request.getSickLeaveBalance());
        }
        if (request.getPaidLeaveBalance() != null) {
            employee.setPaidLeaveBalance(request.getPaidLeaveBalance());
        }
        
        employee = employeeRepository.save(employee);
        log.info("Employee updated: {}", employee.getEmployeeCode());
        
        return mapToResponse(employee);
    }
    
    @Transactional
    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        employee.setActive(false);
        employeeRepository.save(employee);
        
        // Deactivate user account
        if (employee.getUserId() != null) {
            userRepository.findById(employee.getUserId()).ifPresent(user -> {
                user.setActive(false);
                userRepository.save(user);
            });
        }
        
        log.info("Employee deactivated: {}", employee.getEmployeeCode());
    }
    
    @Transactional
    public void activateEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        
        employee.setActive(true);
        employeeRepository.save(employee);
        
        // Activate user account
        if (employee.getUserId() != null) {
            userRepository.findById(employee.getUserId()).ifPresent(user -> {
                user.setActive(true);
                userRepository.save(user);
            });
        }
        
        log.info("Employee activated: {}", employee.getEmployeeCode());
    }
    
    private String generateEmployeeCode() {
        String code;
        do {
            code = "EMP" + String.format("%05d", (int) (Math.random() * 100000));
        } while (employeeRepository.existsByEmployeeCode(code));
        return code;
    }
    
    private EmployeeResponse mapToResponse(Employee employee) {
        String departmentName = null;
        if (employee.getDepartmentId() != null) {
            departmentName = departmentRepository.findById(employee.getDepartmentId())
                .map(com.attendance.system.model.Department::getName)
                .orElse(null);
        }
        
        return EmployeeResponse.builder()
            .id(employee.getId())
            .employeeCode(employee.getEmployeeCode())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .email(employee.getEmail())
            .phone(employee.getPhone())
            .address(employee.getAddress())
            .dateOfBirth(employee.getDateOfBirth())
            .joiningDate(employee.getJoiningDate())
            .departmentId(employee.getDepartmentId())
            .departmentName(departmentName)
            .designation(employee.getDesignation())
            .role(employee.getRole())
            .active(employee.isActive())
            .profilePictureUrl(employee.getProfilePictureUrl())
            .casualLeaveBalance(employee.getCasualLeaveBalance())
            .sickLeaveBalance(employee.getSickLeaveBalance())
            .paidLeaveBalance(employee.getPaidLeaveBalance())
            .createdAt(employee.getCreatedAt())
            .updatedAt(employee.getUpdatedAt())
            .build();
    }
}
