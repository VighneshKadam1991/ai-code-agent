package com.company.aicodeagent.dto;

public class MethodImpactResponse {

    private String className;
    private String methodName;

    public MethodImpactResponse(
            String className,
            String methodName) {

        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}