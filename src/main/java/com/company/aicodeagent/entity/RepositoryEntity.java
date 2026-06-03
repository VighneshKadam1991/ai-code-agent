package com.company.aicodeagent.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repositories")
public class RepositoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoName;

    private String repoUrl;

    private LocalDateTime indexedAt;

    public Long getId() {
        return id;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(
            String repoName) {

        this.repoName = repoName;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(
            String repoUrl) {

        this.repoUrl = repoUrl;
    }

    public LocalDateTime getIndexedAt() {
        return indexedAt;
    }

    public void setIndexedAt(
            LocalDateTime indexedAt) {

        this.indexedAt = indexedAt;
    }
}