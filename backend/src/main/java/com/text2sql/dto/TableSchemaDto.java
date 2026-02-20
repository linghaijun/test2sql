package com.text2sql.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableSchemaDto {
    
    private String tableName;
    private String tableComment;
    private List<ColumnInfo> columns;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ColumnInfo {
        private String columnName;
        private String columnType;
        private Boolean nullable;
        private String defaultValue;
        private String comment;
        private Boolean isPrimaryKey;
    }
}
