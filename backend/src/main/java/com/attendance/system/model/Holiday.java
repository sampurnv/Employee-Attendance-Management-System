package com.attendance.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "holidays")
public class Holiday {
    
    @Id
    private String id;
    
    @Indexed
    private LocalDate date;
    
    private String name;
    private String description;
    
    @Builder.Default
    private boolean active = true;
}
