package com.company.aicodeagent.dto;

public class MethodChangeRequest {

    private String service;

    private String className;

    private String oldMethod;

    private String newMethod;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOldMethod() {
        return oldMethod;
    }

    public void setOldMethod(String oldMethod) {
        this.oldMethod = oldMethod;
    }

    public String getNewMethod() {
        return newMethod;
    }

    public void setNewMethod(String newMethod) {
        this.newMethod = newMethod;
    }
}