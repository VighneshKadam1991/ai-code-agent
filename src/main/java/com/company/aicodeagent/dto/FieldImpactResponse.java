package com.company.aicodeagent.dto;

public class FieldImpactResponse {
    private String className;
    private String fieldName;

    public FieldImpactResponse(
            String className,
            String fieldName) {

        this.className = className;
        this.fieldName = fieldName;
    }

    public String getClassName() {
        return className;
    }

    public String getFieldName() {
        return fieldName;
    }
}
