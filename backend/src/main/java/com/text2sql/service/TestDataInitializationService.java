package com.text2sql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializationService {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Transactional
    public void initializeTestData() {
        log.info("Starting test data initialization...");
        
        try {
            executeScript("db/schema.sql");
            executeScript("db/data.sql");
            
            log.info("Test data initialization completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize test data: {}", e.getMessage());
            throw new RuntimeException("Test data initialization failed", e);
        }
    }
    
    private void executeScript(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        
        String[] statements = sql.split(";");
        
        for (String statement : statements) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                try {
                    jdbcTemplate.execute(trimmed);
                } catch (Exception e) {
                    log.warn("Failed to execute statement: {}. Error: {}", 
                        trimmed.substring(0, Math.min(50, trimmed.length())), e.getMessage());
                }
            }
        }
        
        log.info("Executed script: {}", path);
    }
    
    @Transactional
    public void resetTestData() {
        log.info("Resetting test data...");
        
        try {
            jdbcTemplate.execute("DROP TABLE IF EXISTS orders");
            jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
            jdbcTemplate.execute("DROP TABLE IF EXISTS products");
            jdbcTemplate.execute("DROP TABLE IF EXISTS product_categories");
            jdbcTemplate.execute("DROP TABLE IF EXISTS employees");
            jdbcTemplate.execute("DROP TABLE IF EXISTS departments");
            
            initializeTestData();
            
            log.info("Test data reset completed successfully");
        } catch (Exception e) {
            log.error("Failed to reset test data: {}", e.getMessage());
            throw new RuntimeException("Test data reset failed", e);
        }
    }
}
