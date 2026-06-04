package com.company.aicodeagent.dto;

public class CodeChangeImpact {

    private String repo;

    private String file;

    private Integer line;

    private String oldCode;

    private String newCode;

    private String impactType;

    private boolean requiresCodeChange;

    public boolean isRequiresCodeChange() {
        return requiresCodeChange;
    }

    public void setRequiresCodeChange(boolean requiresCodeChange) {
        this.requiresCodeChange = requiresCodeChange;
    }

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

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public String getImpactType() {
        return impactType;
    }

    public void setImpactType(String impactType) {
        this.impactType = impactType;
    }
}