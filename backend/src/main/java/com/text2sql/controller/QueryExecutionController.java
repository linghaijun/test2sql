package com.text2sql.controller;

import com.text2sql.dto.QueryResultDto;
import com.text2sql.service.QueryExecutionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QueryExecutionController {
    
    private final QueryExecutionService queryExecutionService;
    
    @PostMapping("/execute")
    public ResponseEntity<QueryResultDto> executeQuery(@RequestBody ExecuteRequest request) {
        QueryResultDto result = queryExecutionService.executeQuery(
            request.getConnectionId(),
            request.getSql(),
            request.getPage(),
            request.getPageSize()
        );
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/execute/export")
    public ResponseEntity<String> exportQuery(@RequestBody ExecuteRequest request) {
        QueryResultDto result = queryExecutionService.executeQuery(
            request.getConnectionId(),
            request.getSql()
        );
        
        String csv = queryExecutionService.exportToCSV(result);
        
        String filename = "query_result_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
            ".csv";
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csv);
    }
    
    @Data
    public static class ExecuteRequest {
        private Long connectionId;
        private String sql;
        private Integer page;
        private Integer pageSize;
    }
}
