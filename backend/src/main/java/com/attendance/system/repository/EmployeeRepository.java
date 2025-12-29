package com.attendance.system.repository;

import com.attendance.system.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    
    Optional<Employee> findByEmployeeCode(String employeeCode);
    
    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByUserId(String userId);
    
    List<Employee> findByDepartmentId(String departmentId);
    
    List<Employee> findByActive(boolean active);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmployeeCode(String employeeCode);
}
