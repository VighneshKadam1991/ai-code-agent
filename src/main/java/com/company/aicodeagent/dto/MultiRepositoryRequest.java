package com.company.aicodeagent.dto;

import java.util.List;

public class MultiRepositoryRequest {

    private List<String> repositories;

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(
            List<String> repositories) {

        this.repositories = repositories;
    }
}