package com.text2sql.service;

import com.text2sql.dto.CreateConnectionRequest;
import com.text2sql.dto.DatabaseConnectionDto;
import com.text2sql.entity.DatabaseConnection;
import com.text2sql.exception.ConnectionException;
import com.text2sql.repository.DatabaseConnectionRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseConnectionService {
    
    private final DatabaseConnectionRepository repository;
    private final Map<Long, HikariDataSource> connectionPools = new ConcurrentHashMap<>();
    
    @Transactional
    public DatabaseConnectionDto createConnection(CreateConnectionRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new ConnectionException("Connection with name '" + request.getName() + "' already exists");
        }
        
        DatabaseConnection connection = DatabaseConnection.builder()
            .name(request.getName())
            .type(request.getType())
            .host(request.getHost())
            .port(request.getPort())
            .databaseName(request.getDatabaseName())
            .username(request.getUsername())
            .password(request.getPassword())
            .useSsl(request.getUseSsl())
            .description(request.getDescription())
            .build();
        
        connection = repository.save(connection);
        log.info("Created database connection: {}", connection.getName());
        
        return DatabaseConnectionDto.fromEntity(connection);
    }
    
    @Transactional(readOnly = true)
    public List<DatabaseConnectionDto> getAllConnections() {
        return repository.findAll().stream()
            .map(DatabaseConnectionDto::fromEntity)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public DatabaseConnectionDto getConnectionById(Long id) {
        return repository.findById(id)
            .map(DatabaseConnectionDto::fromEntity)
            .orElseThrow(() -> new ConnectionException("Connection not found with id: " + id));
    }
    
    @Transactional(readOnly = true)
    public DatabaseConnection getConnectionEntityById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ConnectionException("Connection not found with id: " + id));
    }
    
    @Transactional
    public DatabaseConnectionDto updateConnection(Long id, CreateConnectionRequest request) {
        DatabaseConnection connection = repository.findById(id)
            .orElseThrow(() -> new ConnectionException("Connection not found with id: " + id));
        
        if (!connection.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            throw new ConnectionException("Connection with name '" + request.getName() + "' already exists");
        }
        
        closeConnectionPool(id);
        
        connection.setName(request.getName());
        connection.setType(request.getType());
        connection.setHost(request.getHost());
        connection.setPort(request.getPort());
        connection.setDatabaseName(request.getDatabaseName());
        connection.setUsername(request.getUsername());
        connection.setPassword(request.getPassword());
        connection.setUseSsl(request.getUseSsl());
        connection.setDescription(request.getDescription());
        
        connection = repository.save(connection);
        log.info("Updated database connection: {}", connection.getName());
        
        return DatabaseConnectionDto.fromEntity(connection);
    }
    
    @Transactional
    public void deleteConnection(Long id) {
        if (!repository.existsById(id)) {
            throw new ConnectionException("Connection not found with id: " + id);
        }
        
        closeConnectionPool(id);
        repository.deleteById(id);
        log.info("Deleted database connection with id: {}", id);
    }
    
    public boolean testConnection(Long id) {
        DatabaseConnection connection = repository.findById(id)
            .orElseThrow(() -> new ConnectionException("Connection not found with id: " + id));
        
        return testConnection(connection);
    }
    
    public boolean testConnection(DatabaseConnection connection) {
        HikariDataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = createConnectionPool(connection);
            conn = dataSource.getConnection();
            boolean valid = conn.isValid(5);
            log.info("Connection test for '{}' : {}", connection.getName(), valid ? "SUCCESS" : "FAILED");
            return valid;
        } catch (SQLException e) {
            log.error("Connection test failed for '{}': {}", connection.getName(), e.getMessage());
            throw new ConnectionException("Connection test failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.warn("Failed to close connection: {}", e.getMessage());
                }
            }
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.close();
            }
        }
    }
    
    public Connection getConnection(Long connectionId) throws SQLException {
        HikariDataSource dataSource = getOrCreateConnectionPool(connectionId);
        return dataSource.getConnection();
    }
    
    public Connection getConnection(DatabaseConnection connection) throws SQLException {
        HikariDataSource dataSource = createConnectionPool(connection);
        return dataSource.getConnection();
    }
    
    private HikariDataSource getOrCreateConnectionPool(Long connectionId) {
        return connectionPools.computeIfAbsent(connectionId, id -> {
            DatabaseConnection connection = repository.findById(id)
                .orElseThrow(() -> new ConnectionException("Connection not found with id: " + id));
            return createConnectionPool(connection);
        });
    }
    
    private HikariDataSource createConnectionPool(DatabaseConnection connection) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connection.getJdbcUrl());
        config.setUsername(connection.getUsername());
        config.setPassword(connection.getPassword());
        config.setDriverClassName(connection.getDriverClassName());
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1200000);
        config.setPoolName("HikariCP-" + connection.getName());
        
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        log.info("Created connection pool for: {}", connection.getName());
        return new HikariDataSource(config);
    }
    
    private void closeConnectionPool(Long connectionId) {
        HikariDataSource dataSource = connectionPools.remove(connectionId);
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("Closed connection pool for connection id: {}", connectionId);
        }
    }
    
    public void closeAllConnectionPools() {
        connectionPools.forEach((id, dataSource) -> {
            if (!dataSource.isClosed()) {
                dataSource.close();
                log.info("Closed connection pool for connection id: {}", id);
            }
        });
        connectionPools.clear();
    }
}
