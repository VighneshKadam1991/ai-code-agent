package com.company.aicodeagent.dto;

public class ApiImpactResponse {

    private String endpoint;
    private String controller;
    private String repository;
    private String entity;
    private String httpMethod;

    public ApiImpactResponse(
            String endpoint,
            String httpMethod,
            String controller,
            String repository,
            String entity) {

        this.endpoint = endpoint;
        this.controller = controller;
        this.repository = repository;
        this.entity = entity;
        this.httpMethod = httpMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getController() {
        return controller;
    }

    public String getRepository() {
        return repository;
    }

    public String getEntity() {
        return entity;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}