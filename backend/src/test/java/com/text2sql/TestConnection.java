package com.text2sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String[] testConfigs = {
            "jdbc:mysql://localhost:3306/text2sql_app?useSSL=false&serverTimezone=Asia/Shanghai",
            "jdbc:mysql://127.0.0.1:3306/text2sql_app?useSSL=false&serverTimezone=Asia/Shanghai",
            "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=Asia/Shanghai"
        };
        
        String username = "root";
        String password = "your_password";
        
        System.out.println("=== MySQL 连接测试 ===\n");
        
        for (String url : testConfigs) {
            System.out.println("测试连接: " + url);
            System.out.println("用户名: " + username);
            System.out.println("密码: " + password);
            
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("✅ 连接成功！");
                System.out.println("数据库: " + conn.getCatalog());
                System.out.println();
            } catch (SQLException e) {
                System.out.println("❌ 连接失败！");
                System.out.println("错误: " + e.getMessage());
                System.out.println("错误代码: " + e.getErrorCode());
                System.out.println("SQL状态: " + e.getSQLState());
                System.out.println();
            }
        }
        
        System.out.println("\n=== 测试空密码 ===");
        try (Connection conn = DriverManager.getConnection(testConfigs[0], username, "")) {
            System.out.println("✅ 空密码连接成功！");
        } catch (SQLException e) {
            System.out.println("❌ 空密码连接失败: " + e.getMessage());
        }
        
        System.out.println("\n=== 测试密码 'root' ===");
        try (Connection conn = DriverManager.getConnection(testConfigs[0], username, "root")) {
            System.out.println("✅ 密码 'root' 连接成功！");
        } catch (SQLException e) {
            System.out.println("❌ 密码 'root' 连接失败: " + e.getMessage());
        }
    }
}
