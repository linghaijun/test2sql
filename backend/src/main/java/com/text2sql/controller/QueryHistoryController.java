package com.text2sql.controller;

import com.text2sql.entity.QueryHistory;
import com.text2sql.service.QueryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QueryHistoryController {
    
    private final QueryHistoryService historyService;
    
    @GetMapping
    public ResponseEntity<Page<QueryHistory>> getHistory(
            @RequestParam(required = false) Long connectionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        int pageIndex = Math.max(0, page - 1);
        
        Page<QueryHistory> history;
        if (connectionId != null) {
            history = historyService.getHistory(connectionId, pageIndex, size);
        } else {
            Pageable pageable = PageRequest.of(pageIndex, size);
            List<QueryHistory> allHistory = historyService.getRecentHistory();
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allHistory.size());
            List<QueryHistory> pageContent = allHistory.subList(start, end);
            history = new PageImpl<>(pageContent, pageable, allHistory.size());
        }
        
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<QueryHistory>> getRecentHistory() {
        return ResponseEntity.ok(historyService.getRecentHistory());
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<QueryHistory>> searchHistory(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        int pageIndex = Math.max(0, page - 1);
        Page<QueryHistory> history = historyService.searchHistory(keyword, pageIndex, size);
        return ResponseEntity.ok(history);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> clearHistory() {
        historyService.clearHistory();
        return ResponseEntity.noContent().build();
    }
}
