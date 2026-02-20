package com.text2sql.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diagnostics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiagnosticsController {
    
    @GetMapping("/test-db-connection")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection(
            @RequestParam String host,
            @RequestParam Integer port,
            @RequestParam String database,
            @RequestParam String username,
            @RequestParam String password
    ) {
        Map<String, Object> result = new HashMap<>();
        List<String> tests = new ArrayList<>();
        
        String baseUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8",
            host, port, database);
        
        tests.add("测试连接 URL: " + baseUrl);
        tests.add("用户名: " + username);
        tests.add("密码长度: " + password.length() + " 字符");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            tests.add("✅ MySQL 驱动加载成功");
        } catch (ClassNotFoundException e) {
            tests.add("❌ MySQL 驱动加载失败: " + e.getMessage());
            result.put("success", false);
            result.put("tests", tests);
            return ResponseEntity.ok(result);
        }
        
        try (Connection conn = DriverManager.getConnection(baseUrl, username, password)) {
            tests.add("✅ 数据库连接成功！");
            tests.add("数据库: " + conn.getCatalog());
            tests.add("数据库版本: " + conn.getMetaData().getDatabaseProductName() + " " + conn.getMetaData().getDatabaseProductVersion());
            
            result.put("success", true);
            result.put("message", "连接成功");
        } catch (Exception e) {
            tests.add("❌ 连接失败");
            tests.add("错误类型: " + e.getClass().getSimpleName());
            tests.add("错误信息: " + e.getMessage());
            
            if (e.getCause() != null) {
                tests.add("原因: " + e.getCause().getMessage());
            }
            
            result.put("success", false);
            result.put("message", "连接失败: " + e.getMessage());
        }
        
        result.put("tests", tests);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/test-common-passwords")
    public ResponseEntity<Map<String, Object>> testCommonPasswords(
            @RequestParam String host,
            @RequestParam Integer port,
            @RequestParam String username
    ) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> tests = new ArrayList<>();
        
        String[] passwords = {"", "root", "123456", "password", "your_password"};
        
        String url = String.format("jdbc:mysql://%s:%d/mysql?useSSL=false&serverTimezone=Asia/Shanghai",
            host, port);
        
        for (String pwd : passwords) {
            Map<String, Object> test = new HashMap<>();
            test.put("password", pwd.isEmpty() ? "(空)" : pwd);
            
            try (Connection conn = DriverManager.getConnection(url, username, pwd)) {
                test.put("success", true);
                test.put("message", "连接成功！");
            } catch (Exception e) {
                test.put("success", false);
                test.put("message", e.getMessage());
            }
            
            tests.add(test);
        }
        
        result.put("tests", tests);
        return ResponseEntity.ok(result);
    }
}
