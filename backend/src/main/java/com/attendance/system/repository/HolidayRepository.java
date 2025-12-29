package com.attendance.system.repository;

import com.attendance.system.model.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends MongoRepository<Holiday, String> {
    
    Optional<Holiday> findByDate(LocalDate date);
    
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Holiday> findByActive(boolean active);
    
    boolean existsByDate(LocalDate date);
}
