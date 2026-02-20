package com.text2sql.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.text2sql.config.DashScopeConfig;
import com.text2sql.exception.ConnectionException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIClientService {
    
    private final DashScopeConfig config;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build();
    
    private static final String DASHSCOPE_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    public String generateCompletion(String systemPrompt, String userPrompt) {
        try {
            String requestBody = buildRequestBody(systemPrompt, userPrompt);
            
            Request request = new Request.Builder()
                .url(DASHSCOPE_API_URL)
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("DashScope API error: {} - {}", response.code(), errorBody);
                    throw new ConnectionException("AI service error: " + response.code());
                }
                
                String responseBody = response.body().string();
                return parseResponse(responseBody);
            }
        } catch (IOException e) {
            log.error("Failed to call DashScope API: {}", e.getMessage());
            throw new ConnectionException("Failed to call AI service: " + e.getMessage(), e);
        }
    }

    private String buildRequestBody(String systemPrompt, String userPrompt) throws IOException {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", config.getModel());

        ArrayNode messages = objectMapper.createArrayNode();
        messages.add(objectMapper.createObjectNode()
                .put("role", "system")
                .put("content", systemPrompt));
        messages.add(objectMapper.createObjectNode()
                .put("role", "user")
                .put("content", userPrompt));

        ObjectNode input = objectMapper.createObjectNode();
        input.set("messages", messages);
        requestBody.set("input", input);

        ObjectNode parameters = objectMapper.createObjectNode();
        parameters.put("temperature", config.getTemperature());
        parameters.put("max_tokens", config.getMaxTokens());
        parameters.put("result_format", "text");
        requestBody.set("parameters", parameters);

        return objectMapper.writeValueAsString(requestBody);
    }
    
    private String parseResponse(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode output = root.path("output");
        
        if (output.has("text")) {
            return output.get("text").asText();
        } else if (output.has("choices") && output.get("choices").isArray()) {
            JsonNode choices = output.get("choices");
            if (choices.size() > 0) {
                return choices.get(0).path("message").path("content").asText();
            }
        }
        
        log.warn("Unexpected response format: {}", responseBody);
        throw new ConnectionException("Unexpected AI response format");
    }
}
