package com.company.aicodeagent.service;

import com.company.aicodeagent.dto.RepositoryImpactResponse;
import com.company.aicodeagent.entity.RepositoryDependencyEntity;
import com.company.aicodeagent.repository.RepositoryDependencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryImpactService {

    private final RepositoryDependencyRepository
            repositoryDependencyRepository;

    public RepositoryImpactService(
            RepositoryDependencyRepository repositoryDependencyRepository) {

        this.repositoryDependencyRepository =
                repositoryDependencyRepository;
    }

    public List<RepositoryImpactResponse>
    findImpact(
            String repoName) {

        return repositoryDependencyRepository
                .findBySourceRepo(repoName)
                .stream()
                .map(dep ->
                        new RepositoryImpactResponse(
                                dep.getSourceRepo(),
                                dep.getTargetRepo(),
                                dep.getDependencyType()))
                .toList();
    }
}