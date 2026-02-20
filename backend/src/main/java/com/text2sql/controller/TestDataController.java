package com.text2sql.controller;

import com.text2sql.service.TestDataInitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test-data")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestDataController {
    
    private final TestDataInitializationService testDataService;
    
    @PostMapping("/initialize")
    public ResponseEntity<Map<String, String>> initializeTestData() {
        testDataService.initializeTestData();
        return ResponseEntity.ok(Map.of(
            "message", "Test data initialized successfully"
        ));
    }
    
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetTestData() {
        testDataService.resetTestData();
        return ResponseEntity.ok(Map.of(
            "message", "Test data reset successfully"
        ));
    }
}
