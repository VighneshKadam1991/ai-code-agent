package com.company.aicodeagent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "api_endpoints")
public class ApiEndpointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoName;

    private String controllerClass;

    private String endpoint;

    private String httpMethod;

    public Long getId() {
        return id;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(String controllerClass) {
        this.controllerClass = controllerClass;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}