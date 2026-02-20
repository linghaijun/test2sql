package com.text2sql.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.text2sql.entity.DatabaseConnection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConnectionRequest {
    
    @NotBlank(message = "Connection name is required")
    private String name;
    
    @NotNull(message = "Database type is required")
    private DatabaseConnection.DatabaseType type;
    
    @NotBlank(message = "Host is required")
    private String host;
    
    @NotNull(message = "Port is required")
    private Integer port;
    
    @NotBlank(message = "Database name is required")
    private String databaseName;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private Boolean useSsl = false;
    
    private String description;
}
