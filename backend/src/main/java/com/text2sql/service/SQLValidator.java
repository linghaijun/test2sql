package com.text2sql.service;

import com.text2sql.exception.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class SQLValidator {
    
    private static final Pattern[] DANGEROUS_PATTERNS = {
        Pattern.compile("\\bDROP\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bDELETE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bINSERT\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bUPDATE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bALTER\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bCREATE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bTRUNCATE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bGRANT\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bREVOKE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bEXEC\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bEXECUTE\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile(";\\s*DROP", Pattern.CASE_INSENSITIVE),
        Pattern.compile(";\\s*DELETE", Pattern.CASE_INSENSITIVE),
        Pattern.compile(";\\s*INSERT", Pattern.CASE_INSENSITIVE),
        Pattern.compile(";\\s*UPDATE", Pattern.CASE_INSENSITIVE)
    };
    
    private static final Pattern SELECT_PATTERN = 
        Pattern.compile("^\\s*SELECT\\s+", Pattern.CASE_INSENSITIVE);
    
    public void validate(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new ConnectionException("SQL query cannot be empty");
        }
        
        String trimmedSql = sql.trim();
        
        if (!SELECT_PATTERN.matcher(trimmedSql).find()) {
            throw new ConnectionException("Only SELECT queries are allowed");
        }
        
        for (Pattern pattern : DANGEROUS_PATTERNS) {
            if (pattern.matcher(trimmedSql).find()) {
                log.warn("Dangerous SQL pattern detected: {}", pattern.pattern());
                throw new ConnectionException("Dangerous SQL operation detected. Only SELECT queries are allowed.");
            }
        }
        
        log.debug("SQL validation passed: {}", sql);
    }
    
    public boolean isSafe(String sql) {
        try {
            validate(sql);
            return true;
        } catch (ConnectionException e) {
            return false;
        }
    }
}
