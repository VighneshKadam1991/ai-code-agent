package com.company.aicodeagent.dto;

public class EntityChangeRequest {

    private String oldEntity;

    private String newEntity;

    public String getOldEntity() {
        return oldEntity;
    }

    public void setOldEntity(String oldEntity) {
        this.oldEntity = oldEntity;
    }

    public String getNewEntity() {
        return newEntity;
    }

    public void setNewEntity(String newEntity) {
        this.newEntity = newEntity;
    }
}