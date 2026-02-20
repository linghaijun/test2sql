package com.text2sql.service;

import com.text2sql.dto.QueryResultDto;
import com.text2sql.exception.ConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryExecutionService {
    
    private final DatabaseConnectionService connectionService;
    private final SQLValidator sqlValidator;
    
    @Value("${query.execution.timeout:30}")
    private int queryTimeout;
    
    @Value("${query.execution.max-rows:10000}")
    private int maxRows;
    
    public QueryResultDto executeQuery(Long connectionId, String sql) {
        return executeQuery(connectionId, sql, null, null);
    }
    
    public QueryResultDto executeQuery(Long connectionId, String sql, Integer page, Integer pageSize) {
        sqlValidator.validate(sql);
        
        long startTime = System.currentTimeMillis();
        
        try (Connection conn = connectionService.getConnection(connectionId)) {
            
            if (page != null && pageSize != null && page > 0 && pageSize > 0) {
                sql = addPagination(sql, page, pageSize);
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setQueryTimeout(queryTimeout);
                stmt.setMaxRows(maxRows);
                
                long executeStart = System.currentTimeMillis();
                boolean hasResultSet = stmt.execute();
                long executionTime = System.currentTimeMillis() - executeStart;
                
                if (hasResultSet) {
                    try (ResultSet rs = stmt.getResultSet()) {
                        QueryResultDto result = processResultSet(rs, sql, page, pageSize);
                        result.setExecutionTime(executionTime);
                        
                        long totalTime = System.currentTimeMillis() - startTime;
                        log.info("Query executed successfully in {}ms (total: {}ms)", executionTime, totalTime);
                        
                        return result;
                    }
                } else {
                    return QueryResultDto.empty(sql);
                }
            }
        } catch (SQLException e) {
            log.error("Query execution failed: {}", e.getMessage());
            throw new ConnectionException("Query execution failed: " + e.getMessage(), e);
        }
    }
    
    private QueryResultDto processResultSet(ResultSet rs, String sql, Integer page, Integer pageSize) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        List<String> columns = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            columns.add(metaData.getColumnLabel(i));
        }
        
        List<Map<String, Object>> rows = new ArrayList<>();
        int rowCount = 0;
        
        while (rs.next() && rowCount < maxRows) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = rs.getObject(i);
                
                if (value instanceof Timestamp) {
                    value = ((Timestamp) value).toLocalDateTime();
                } else if (value instanceof java.sql.Date) {
                    value = ((java.sql.Date) value).toLocalDate();
                } else if (value instanceof Time) {
                    value = ((Time) value).toLocalTime();
                }
                
                row.put(columnName, value);
            }
            rows.add(row);
            rowCount++;
        }
        
        QueryResultDto.QueryResultDtoBuilder builder = QueryResultDto.builder()
            .columns(columns)
            .rows(rows)
            .totalRows(rows.size())
            .sql(sql);
        
        if (page != null && pageSize != null) {
            builder.page(page).pageSize(pageSize);
        }
        
        return builder.build();
    }
    
    private String addPagination(String sql, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        
        sql = sql.trim();
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        
        return sql + String.format(" LIMIT %d OFFSET %d", pageSize, offset);
    }
    
    public String exportToCSV(QueryResultDto result) {
        StringBuilder csv = new StringBuilder();
        
        csv.append(String.join(",", result.getColumns())).append("\n");
        
        for (Map<String, Object> row : result.getRows()) {
            List<String> values = new ArrayList<>();
            for (String column : result.getColumns()) {
                Object value = row.get(column);
                String strValue = value == null ? "" : value.toString();
                
                if (strValue.contains(",") || strValue.contains("\"") || strValue.contains("\n")) {
                    strValue = "\"" + strValue.replace("\"", "\"\"") + "\"";
                }
                values.add(strValue);
            }
            csv.append(String.join(",", values)).append("\n");
        }
        
        return csv.toString();
    }
}
