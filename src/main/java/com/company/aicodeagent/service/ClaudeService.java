package com.company.aicodeagent.service;

import com.company.aicodeagent.config.ClaudeConfig;
import com.company.aicodeagent.dto.ClaudeResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Service
public class ClaudeService {

    private final ClaudeConfig config;

    private final WebClient webClient;

    public ClaudeService(ClaudeConfig config) {

        this.config = config;

        this.webClient =
                WebClient.builder()
                        .baseUrl("https://api.anthropic.com")
                        .defaultHeader(
                                HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE)
                        .defaultHeader(
                                "x-api-key",
                                config.getApiKey())
                        .defaultHeader(
                                "anthropic-version",
                                "2023-06-01")
                        .build();
    }

    public String askClaude(String prompt) {

        Map<String, Object> request =
                Map.of(
                        "model",
                        config.getModel(),
                        "max_tokens",
                        4000,
                        "messages",
                        new Object[]{
                                Map.of(
                                        "role",
                                        "user",
                                        "content",
                                        prompt)
                        }
                );

        ClaudeResponse response =
                webClient
                        .post()
                        .uri("/v1/messages")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(ClaudeResponse.class)
                        .block();

        if (response == null ||
                response.getContent() == null ||
                response.getContent().isEmpty()) {

            return "No response from Claude";
        }

        return response.getContent()
                .get(0)
                .getText();
    }
}