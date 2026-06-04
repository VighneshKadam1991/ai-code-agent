package com.company.aicodeagent.dto;

public class RepositoryImpactResponse {

    private String sourceRepo;

    private String targetRepo;

    private String dependencyType;

    public RepositoryImpactResponse(
            String sourceRepo,
            String targetRepo,
            String dependencyType) {

        this.sourceRepo = sourceRepo;
        this.targetRepo = targetRepo;
        this.dependencyType = dependencyType;
    }

    public String getSourceRepo() {
        return sourceRepo;
    }

    public String getTargetRepo() {
        return targetRepo;
    }

    public String getDependencyType() {
        return dependencyType;
    }
}