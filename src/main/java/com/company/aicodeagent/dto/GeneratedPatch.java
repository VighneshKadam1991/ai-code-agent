package com.company.aicodeagent.dto;

public class GeneratedPatch {

    private String repo;

    private String file;

    private String originalFile;

    private String updatedFile;

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

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    public String getUpdatedFile() {
        return updatedFile;
    }

    public void setUpdatedFile(String updatedFile) {
        this.updatedFile = updatedFile;
    }
}