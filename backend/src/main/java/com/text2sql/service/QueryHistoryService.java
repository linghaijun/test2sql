package com.text2sql.service;

import com.text2sql.entity.QueryHistory;
import com.text2sql.repository.QueryHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryHistoryService {
    
    private final QueryHistoryRepository repository;
    
    @Transactional
    public QueryHistory saveHistory(Long connectionId, String naturalLanguageQuery, String generatedSql) {
        QueryHistory history = QueryHistory.builder()
            .connectionId(connectionId)
            .naturalLanguageQuery(naturalLanguageQuery)
            .generatedSql(generatedSql)
            .executed(false)
            .build();
        
        history = repository.save(history);
        log.debug("Saved query history: {}", history.getId());
        
        return history;
    }
    
    @Transactional
    public void updateExecutionResult(Long historyId, boolean executed, Integer resultCount, Long executionTimeMs) {
        repository.findById(historyId).ifPresent(history -> {
            history.setExecuted(executed);
            history.setResultCount(resultCount);
            history.setExecutionTimeMs(executionTimeMs);
            repository.save(history);
        });
    }
    
    @Transactional(readOnly = true)
    public Page<QueryHistory> getHistory(Long connectionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByConnectionIdOrderByCreatedAtDesc(connectionId, pageable);
    }
    
    @Transactional(readOnly = true)
    public List<QueryHistory> getRecentHistory() {
        return repository.findTop20ByOrderByCreatedAtDesc();
    }
    
    @Transactional(readOnly = true)
    public Page<QueryHistory> searchHistory(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.searchByKeyword(keyword, pageable);
    }
    
    @Transactional
    public void clearHistory() {
        repository.deleteAll();
        log.info("Cleared all query history");
    }
}
