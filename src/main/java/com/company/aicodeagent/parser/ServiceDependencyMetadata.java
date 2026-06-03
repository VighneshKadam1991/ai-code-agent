package com.company.aicodeagent.parser;

public class ServiceDependencyMetadata {

    private String sourceService;

    private String targetService;

    private String clientType;

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(
            String sourceService) {

        this.sourceService =
                sourceService;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(
            String targetService) {

        this.targetService =
                targetService;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(
            String clientType) {

        this.clientType =
                clientType;
    }
}