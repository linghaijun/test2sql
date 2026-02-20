package com.text2sql.service;

import com.text2sql.dto.TableSchemaDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromptTemplateService {
    
    private static final String SYSTEM_PROMPT = """
        你是一个专业的 SQL 查询生成助手。你的任务是将用户的自然语言查询转换为准确的 SQL SELECT 语句。
        
        规则：
        1. 只生成 SELECT 查询，不要生成 INSERT、UPDATE、DELETE、DROP 等修改数据的语句
        2. 使用标准 SQL 语法，避免数据库特定的函数
        3. 如果用户的问题不清楚，生成最合理的查询
        4. 始终返回有效的 SQL 语句
        5. 表名和字段名必须与提供的 schema 完全匹配
        
        输出格式：
        - 只输出 SQL 语句，不要包含任何解释或额外的文本
        - SQL 语句应该是单行的，以分号结尾
        """;
    
    public String buildSqlGenerationPrompt(String userQuery, List<TableSchemaDto> schemas) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("数据库表结构信息：\n\n");
        
        for (TableSchemaDto schema : schemas) {
            prompt.append(String.format("表名: %s", schema.getTableName()));
            if (schema.getTableComment() != null && !schema.getTableComment().isEmpty()) {
                prompt.append(String.format(" (%s)", schema.getTableComment()));
            }
            prompt.append("\n");
            
            if (schema.getColumns() != null && !schema.getColumns().isEmpty()) {
                prompt.append("字段:\n");
                for (TableSchemaDto.ColumnInfo column : schema.getColumns()) {
                    prompt.append(String.format("  - %s: %s", 
                        column.getColumnName(), 
                        column.getColumnType()));
                    
                    if (Boolean.TRUE.equals(column.getIsPrimaryKey())) {
                        prompt.append(" (主键)");
                    }
                    if (column.getComment() != null && !column.getComment().isEmpty()) {
                        prompt.append(String.format(" - %s", column.getComment()));
                    }
                    prompt.append("\n");
                }
            }
            prompt.append("\n");
        }
        
        prompt.append(String.format("用户查询: %s\n\n", userQuery));
        prompt.append("请根据上面的表结构信息，将用户的查询转换为 SQL SELECT 语句：");
        
        return prompt.toString();
    }
    
    public String buildSqlExplanationPrompt(String sql) {
        return String.format("""
            请用简洁的中文解释以下 SQL 查询的作用：
            
            %s
            
            解释要点：
            1. 查询哪些表
            2. 使用了哪些字段
            3. 查询条件是什么
            4. 结果如何排序或分组（如果有）
            """, sql);
    }
    
    public String getSystemPrompt() {
        return SYSTEM_PROMPT;
    }
    
    public List<String> getExampleQueries() {
        return List.of(
            "查询所有员工信息",
            "查找年龄大于30岁的员工",
            "统计各部门的员工数量",
            "查询价格高于1000的产品",
            "查询员工姓名及其所属部门名称"
        );
    }
}
