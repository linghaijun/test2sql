package com.text2sql.repository;

import com.text2sql.entity.DatabaseConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseConnectionRepository extends JpaRepository<DatabaseConnection, Long> {
    
    List<DatabaseConnection> findByType(DatabaseConnection.DatabaseType type);
    
    Optional<DatabaseConnection> findByName(String name);
    
    boolean existsByName(String name);
}
