package com.company.aicodeagent.dto;

public class MethodRenameRequest {

    private String entity;

    private String oldMethod;

    private String newMethod;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
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