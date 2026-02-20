package com.text2sql.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.text2sql.entity.DatabaseConnection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseConnectionDto {
    
    private Long id;
    private String name;
    private DatabaseConnection.DatabaseType type;
    private String host;
    private Integer port;
    private String databaseName;
    private String username;
    private Boolean useSsl;
    private String description;
    
    public static DatabaseConnectionDto fromEntity(DatabaseConnection entity) {
        return DatabaseConnectionDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .type(entity.getType())
            .host(entity.getHost())
            .port(entity.getPort())
            .databaseName(entity.getDatabaseName())
            .username(entity.getUsername())
            .useSsl(entity.getUseSsl())
            .description(entity.getDescription())
            .build();
    }
}
