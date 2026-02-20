package com.text2sql.service;

import com.text2sql.dto.TableSchemaDto;
import com.text2sql.entity.DatabaseConnection;
import com.text2sql.exception.ConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchemaService {
    
    private final DatabaseConnectionService connectionService;
    
    @Cacheable(value = "tableSchemas", key = "#connectionId")
    public List<String> getTableNames(Long connectionId) {
        DatabaseConnection connection = connectionService.getConnectionEntityById(connectionId);
        return getTableNames(connection);
    }
    
    public List<String> getTableNames(DatabaseConnection connection) {
        List<String> tableNames = new ArrayList<>();
        
        try (Connection conn = connectionService.getConnection(connection)) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = connection.getDatabaseName();
            String schema = connection.getType() == DatabaseConnection.DatabaseType.POSTGRESQL 
                ? "public" 
                : null;
            
            try (ResultSet tables = metaData.getTables(catalog, schema, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    tableNames.add(tableName);
                }
            }
            
            log.info("Retrieved {} tables from database: {}", tableNames.size(), connection.getName());
        } catch (SQLException e) {
            log.error("Failed to get table names: {}", e.getMessage());
            throw new ConnectionException("Failed to get table names: " + e.getMessage(), e);
        }
        
        return tableNames;
    }
    
    @Cacheable(value = "tableSchema", key = "#connectionId + '_' + #tableName")
    public TableSchemaDto getTableSchema(Long connectionId, String tableName) {
        DatabaseConnection connection = connectionService.getConnectionEntityById(connectionId);
        return getTableSchema(connection, tableName);
    }
    
    public TableSchemaDto getTableSchema(DatabaseConnection connection, String tableName) {
        TableSchemaDto.TableSchemaDtoBuilder schemaBuilder = TableSchemaDto.builder()
            .tableName(tableName);
        
        List<TableSchemaDto.ColumnInfo> columns = new ArrayList<>();
        Set<String> primaryKeys = new HashSet<>();
        
        try (Connection conn = connectionService.getConnection(connection)) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = connection.getDatabaseName();
            String schema = connection.getType() == DatabaseConnection.DatabaseType.POSTGRESQL 
                ? "public" 
                : null;
            
            try (ResultSet pkRs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
                while (pkRs.next()) {
                    primaryKeys.add(pkRs.getString("COLUMN_NAME"));
                }
            }
            
            try (ResultSet columnsRs = metaData.getColumns(catalog, schema, tableName, "%")) {
                while (columnsRs.next()) {
                    TableSchemaDto.ColumnInfo columnInfo = TableSchemaDto.ColumnInfo.builder()
                        .columnName(columnsRs.getString("COLUMN_NAME"))
                        .columnType(columnsRs.getString("TYPE_NAME"))
                        .nullable("YES".equalsIgnoreCase(columnsRs.getString("IS_NULLABLE")))
                        .defaultValue(columnsRs.getString("COLUMN_DEF"))
                        .comment(columnsRs.getString("REMARKS"))
                        .isPrimaryKey(primaryKeys.contains(columnsRs.getString("COLUMN_NAME")))
                        .build();
                    
                    columns.add(columnInfo);
                }
            }
            
            try (ResultSet tablesRs = metaData.getTables(catalog, schema, tableName, new String[]{"TABLE"})) {
                if (tablesRs.next()) {
                    schemaBuilder.tableComment(tablesRs.getString("REMARKS"));
                }
            }
            
        } catch (SQLException e) {
            log.error("Failed to get table schema for {}: {}", tableName, e.getMessage());
            throw new ConnectionException("Failed to get table schema: " + e.getMessage(), e);
        }
        
        schemaBuilder.columns(columns);
        log.info("Retrieved schema for table: {} with {} columns", tableName, columns.size());
        
        return schemaBuilder.build();
    }
    
    @Cacheable(value = "allSchemas", key = "#connectionId")
    public List<TableSchemaDto> getAllTableSchemas(Long connectionId) {
        List<String> tableNames = getTableNames(connectionId);
        List<TableSchemaDto> schemas = new ArrayList<>();
        
        DatabaseConnection connection = connectionService.getConnectionEntityById(connectionId);
        for (String tableName : tableNames) {
            schemas.add(getTableSchema(connection, tableName));
        }
        
        return schemas;
    }
    
    public void clearSchemaCache(Long connectionId) {
        log.info("Schema cache cleared for connection: {}", connectionId);
    }
}
