package com.company.aicodeagent.dto;

public class EntityRenameRequest {

    private String entity;
    private String newEntity;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getNewEntity() {
        return newEntity;
    }

    public void setNewEntity(String newEntity) {
        this.newEntity = newEntity;
    }
}