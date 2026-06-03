package com.company.aicodeagent.dto;

public record ApiSearchResponse(
        String repoName,
        String controllerClass,
        String endpoint,
        String httpMethod) {
}