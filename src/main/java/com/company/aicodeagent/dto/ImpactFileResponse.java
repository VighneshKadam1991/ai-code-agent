package com.company.aicodeagent.dto;

public class ImpactFileResponse {

    private String className;
    private String filePath;
    private String reason;

    public ImpactFileResponse(
            String className,
            String filePath,
            String reason) {

        this.className = className;
        this.filePath = filePath;
        this.reason = reason;
    }

    public String getClassName() {
        return className;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getReason() {
        return reason;
    }
}