package com.text2sql.controller;

import com.text2sql.dto.CreateConnectionRequest;
import com.text2sql.dto.DatabaseConnectionDto;
import com.text2sql.service.DatabaseConnectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DatabaseConnectionController {
    
    private final DatabaseConnectionService connectionService;
    
    @PostMapping
    public ResponseEntity<DatabaseConnectionDto> createConnection(@Valid @RequestBody CreateConnectionRequest request) {
        DatabaseConnectionDto connection = connectionService.createConnection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(connection);
    }
    
    @GetMapping
    public ResponseEntity<List<DatabaseConnectionDto>> getAllConnections() {
        List<DatabaseConnectionDto> connections = connectionService.getAllConnections();
        return ResponseEntity.ok(connections);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DatabaseConnectionDto> getConnectionById(@PathVariable Long id) {
        DatabaseConnectionDto connection = connectionService.getConnectionById(id);
        return ResponseEntity.ok(connection);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DatabaseConnectionDto> updateConnection(
            @PathVariable Long id,
            @Valid @RequestBody CreateConnectionRequest request) {
        DatabaseConnectionDto connection = connectionService.updateConnection(id, request);
        return ResponseEntity.ok(connection);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        connectionService.deleteConnection(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/test")
    public ResponseEntity<Map<String, Object>> testConnection(@PathVariable Long id) {
        try {
            boolean success = connectionService.testConnection(id);
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", success ? "Connection successful" : "Connection failed"
            ));
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg += " - " + e.getCause().getMessage();
            }
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Connection failed: " + errorMsg
            ));
        }
    }
    
    @PostMapping("/test-direct")
    public ResponseEntity<Map<String, Object>> testConnectionDirect(@Valid @RequestBody CreateConnectionRequest request) {
        try {
            com.text2sql.entity.DatabaseConnection connection = 
                com.text2sql.entity.DatabaseConnection.builder()
                    .name(request.getName())
                    .type(request.getType())
                    .host(request.getHost())
                    .port(request.getPort())
                    .databaseName(request.getDatabaseName())
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .useSsl(request.getUseSsl())
                    .build();
            
            boolean success = connectionService.testConnection(connection);
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", success ? "Connection successful" : "Connection failed",
                "details", Map.of(
                    "host", request.getHost(),
                    "port", request.getPort(),
                    "database", request.getDatabaseName(),
                    "username", request.getUsername()
                )
            ));
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            String errorType = e.getClass().getSimpleName();
            if (e.getCause() != null) {
                errorMsg += " - Cause: " + e.getCause().getMessage();
                errorType += " / " + e.getCause().getClass().getSimpleName();
            }
            
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Connection failed: " + errorMsg,
                "errorType", errorType,
                "details", Map.of(
                    "host", request.getHost(),
                    "port", request.getPort(),
                    "database", request.getDatabaseName(),
                    "username", request.getUsername()
                )
            ));
        }
    }
}
