package com.text2sql.controller;

import com.text2sql.service.NLToSQLService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nl2sql")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NLToSQLController {
    
    private final NLToSQLService nlToSQLService;
    
    @PostMapping("/convert")
    public ResponseEntity<Map<String, String>> convertToSQL(@RequestBody ConvertRequest request) {
        String sql = nlToSQLService.convertToSQL(request.getQuery(), request.getConnectionId());
        return ResponseEntity.ok(Map.of("sql", sql));
    }
    
    @PostMapping("/explain")
    public ResponseEntity<Map<String, String>> explainSQL(@RequestBody ExplainRequest request) {
        String explanation = nlToSQLService.explainSQL(request.getSql());
        return ResponseEntity.ok(Map.of("explanation", explanation));
    }
    
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getQuerySuggestions() {
        List<String> suggestions = nlToSQLService.getQuerySuggestions();
        return ResponseEntity.ok(suggestions);
    }
    
    @Data
    public static class ConvertRequest {
        private String query;
        private Long connectionId;
    }
    
    @Data
    public static class ExplainRequest {
        private String sql;
    }
}
