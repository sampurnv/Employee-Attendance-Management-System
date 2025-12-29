package com.attendance.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class EmployeeAttendanceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeAttendanceSystemApplication.class, args);
    }
}
