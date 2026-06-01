package com.company.aicodeagent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClaudeConfig {

    @Value("${anthropic.api-key}")
    private String apiKey;

    @Value("${claude.model}")
    private String model;

    public String getApiKey() {
        return apiKey;
    }

    public String getModel() {
        return model;
    }
}