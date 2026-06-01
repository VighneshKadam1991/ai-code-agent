package com.company.aicodeagent.dto;

public class AiAnalysisResponse {

    private String response;

    public AiAnalysisResponse() {
    }

    public AiAnalysisResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}