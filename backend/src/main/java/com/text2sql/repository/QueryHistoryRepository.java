package com.text2sql.repository;

import com.text2sql.entity.QueryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QueryHistoryRepository extends JpaRepository<QueryHistory, Long> {
    
    Page<QueryHistory> findByConnectionIdOrderByCreatedAtDesc(Long connectionId, Pageable pageable);
    
    List<QueryHistory> findTop20ByOrderByCreatedAtDesc();
    
    @Query("SELECT h FROM QueryHistory h WHERE " +
           "LOWER(h.naturalLanguageQuery) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.generatedSql) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY h.createdAt DESC")
    Page<QueryHistory> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    void deleteByCreatedAtBefore(LocalDateTime date);
}
