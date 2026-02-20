package com.text2sql.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Table(name = "query_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long connectionId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String naturalLanguageQuery;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String generatedSql;
    
    @Column
    @Builder.Default
    private Boolean executed = false;
    
    @Column
    private Integer resultCount;
    
    @Column
    private Long executionTimeMs;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
