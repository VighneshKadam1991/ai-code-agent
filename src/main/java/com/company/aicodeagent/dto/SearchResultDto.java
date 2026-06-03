package com.company.aicodeagent.dto;

public class SearchResultDto {

    private String repoName;

    private String className;

    private String filePath;

    public SearchResultDto(
            String repoName,
            String className,
            String filePath) {

        this.repoName = repoName;
        this.className = className;
        this.filePath = filePath;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getClassName() {
        return className;
    }

    public String getFilePath() {
        return filePath;
    }
}