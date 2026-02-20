package com.text2sql.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.alibaba.dashscope")
public class DashScopeConfig {
    
    private String apiKey;
    private String model = "qwen-plus";
    private Double temperature = 0.7;
    private Integer maxTokens = 2000;
}
