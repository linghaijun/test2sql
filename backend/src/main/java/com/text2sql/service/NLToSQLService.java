package com.text2sql.service;

import com.text2sql.dto.TableSchemaDto;
import com.text2sql.entity.QueryHistory;
import com.text2sql.exception.ConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class NLToSQLService {
    
    private final AIClientService aiClientService;
    private final PromptTemplateService promptTemplateService;
    private final SchemaService schemaService;
    private final QueryHistoryService queryHistoryService;
    
    private static final Pattern SQL_PATTERN = Pattern.compile(
        "(SELECT\\s+.+?\\s+FROM\\s+[^;]+;?)",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    public String convertToSQL(String naturalLanguageQuery, Long connectionId) {
        List<TableSchemaDto> schemas = schemaService.getAllTableSchemas(connectionId);
        
        if (schemas.isEmpty()) {
            throw new ConnectionException("No tables found in the database");
        }
        
        String systemPrompt = promptTemplateService.getSystemPrompt();
        String userPrompt = promptTemplateService.buildSqlGenerationPrompt(naturalLanguageQuery, schemas);
        
        log.info("Converting natural language to SQL: {}", naturalLanguageQuery);
        
        String aiResponse = aiClientService.generateCompletion(systemPrompt, userPrompt);
        String sql = extractSQL(aiResponse);
        
        log.info("Generated SQL: {}", sql);
        
        try {
            queryHistoryService.saveHistory(connectionId, naturalLanguageQuery, sql);
        } catch (Exception e) {
            log.warn("Failed to save query history: {}", e.getMessage());
        }
        
        return sql;
    }
    
    public String explainSQL(String sql) {
        String systemPrompt = "你是一个 SQL 解释专家。请用简洁的中文解释 SQL 查询的作用。";
        String userPrompt = promptTemplateService.buildSqlExplanationPrompt(sql);
        
        log.info("Explaining SQL: {}", sql);
        
        String explanation = aiClientService.generateCompletion(systemPrompt, userPrompt);
        log.info("SQL explanation: {}", explanation);
        
        return explanation;
    }
    
    public List<String> getQuerySuggestions() {
        return promptTemplateService.getExampleQueries();
    }
    
    private String extractSQL(String aiResponse) {
        aiResponse = aiResponse.trim();
        
        Matcher matcher = SQL_PATTERN.matcher(aiResponse);
        if (matcher.find()) {
            String sql = matcher.group(1);
            if (!sql.endsWith(";")) {
                sql += ";";
            }
            return sql.trim();
        }
        
        if (aiResponse.toUpperCase().contains("SELECT")) {
            String cleaned = aiResponse
                .replaceAll("```sql\\s*", "")
                .replaceAll("```\\s*", "")
                .replaceAll("^(Here is|以下是|The SQL is).*?:\\s*", "")
                .trim();
            
            if (!cleaned.endsWith(";")) {
                cleaned += ";";
            }
            
            return cleaned;
        }
        
        throw new ConnectionException("Failed to extract valid SQL from AI response: " + aiResponse);
    }
}
