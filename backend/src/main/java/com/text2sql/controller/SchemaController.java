package com.text2sql.controller;

import com.text2sql.dto.TableSchemaDto;
import com.text2sql.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schema")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SchemaController {
    
    private final SchemaService schemaService;
    
    @GetMapping("/{connectionId}/tables")
    public ResponseEntity<List<String>> getTableNames(@PathVariable Long connectionId) {
        List<String> tables = schemaService.getTableNames(connectionId);
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/{connectionId}/tables/{tableName}")
    public ResponseEntity<TableSchemaDto> getTableSchema(
            @PathVariable Long connectionId,
            @PathVariable String tableName) {
        TableSchemaDto schema = schemaService.getTableSchema(connectionId, tableName);
        return ResponseEntity.ok(schema);
    }
    
    @GetMapping("/{connectionId}/schemas")
    public ResponseEntity<List<TableSchemaDto>> getAllTableSchemas(@PathVariable Long connectionId) {
        List<TableSchemaDto> schemas = schemaService.getAllTableSchemas(connectionId);
        return ResponseEntity.ok(schemas);
    }
    
    @DeleteMapping("/{connectionId}/cache")
    public ResponseEntity<Void> clearSchemaCache(@PathVariable Long connectionId) {
        schemaService.clearSchemaCache(connectionId);
        return ResponseEntity.noContent().build();
    }
}
