package com.text2sql.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryResultDto {
    
    private List<String> columns;
    private List<Map<String, Object>> rows;
    private Integer totalRows;
    private Integer page;
    private Integer pageSize;
    private Long executionTime;
    private String sql;
    
    public static QueryResultDto of(List<String> columns, List<Map<String, Object>> rows, String sql) {
        return QueryResultDto.builder()
            .columns(columns)
            .rows(rows)
            .totalRows(rows.size())
            .sql(sql)
            .build();
    }
    
    public static QueryResultDto empty(String sql) {
        return QueryResultDto.builder()
            .columns(List.of())
            .rows(List.of())
            .totalRows(0)
            .sql(sql)
            .build();
    }
}
