package com.company.aicodeagent.dto;

public class ServiceApiChangeRequest {

    private String service;

    private String oldEndpoint;

    private String newEndpoint;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOldEndpoint() {
        return oldEndpoint;
    }

    public void setOldEndpoint(String oldEndpoint) {
        this.oldEndpoint = oldEndpoint;
    }

    public String getNewEndpoint() {
        return newEndpoint;
    }

    public void setNewEndpoint(String newEndpoint) {
        this.newEndpoint = newEndpoint;
    }
}