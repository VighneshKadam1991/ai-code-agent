package com.company.aicodeagent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service_dependencies")
public class ServiceDependencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoName;

    private String sourceService;

    private String targetService;

    private String clientType;

    public Long getId() {
        return id;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}