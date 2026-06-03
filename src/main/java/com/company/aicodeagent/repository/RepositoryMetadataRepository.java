package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryMetadataRepository
        extends JpaRepository<RepositoryEntity, Long> {

    boolean existsByRepoName(
            String repoName);
}