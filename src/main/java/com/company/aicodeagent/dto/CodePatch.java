package com.company.aicodeagent.dto;

public class CodePatch {

    private String repo;

    private String file;

    private String originalCode;

    private String updatedCode;

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getOriginalCode() {
        return originalCode;
    }

    public void setOriginalCode(String originalCode) {
        this.originalCode = originalCode;
    }

    public String getUpdatedCode() {
        return updatedCode;
    }

    public void setUpdatedCode(String updatedCode) {
        this.updatedCode = updatedCode;
    }
}