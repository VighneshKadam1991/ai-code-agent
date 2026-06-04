package com.company.aicodeagent.dto;

public class FieldChangeRequest {

    private String entity;

    private String oldField;

    private String newField;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getOldField() {
        return oldField;
    }

    public void setOldField(String oldField) {
        this.oldField = oldField;
    }

    public String getNewField() {
        return newField;
    }

    public void setNewField(String newField) {
        this.newField = newField;
    }
}