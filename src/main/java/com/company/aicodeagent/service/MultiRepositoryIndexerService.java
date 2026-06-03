package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.RepositoryEntity;
import com.company.aicodeagent.repository.RepositoryMetadataRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

@Service
public class MultiRepositoryIndexerService {

    private final GitCloneService gitCloneService;

    private final JavaIndexerService javaIndexerService;

    private final RepositoryMetadataRepository repositoryRepository;

    public MultiRepositoryIndexerService(
            GitCloneService gitCloneService,
            JavaIndexerService javaIndexerService,
            RepositoryMetadataRepository repositoryRepository) {

        this.gitCloneService = gitCloneService;
        this.javaIndexerService = javaIndexerService;
        this.repositoryRepository =
                repositoryRepository;
    }

    public void indexRepository(
            String repoUrl)
            throws Exception {



        String repoName =
                extractRepoName(repoUrl);

        if (repositoryRepository
                .existsByRepoName(repoName)) {

            System.out.println(
                    "REPOSITORY ALREADY INDEXED = "
                            + repoName);

            return;
        }

        File repoFolder =
                gitCloneService.cloneRepository(
                        repoUrl);

        javaIndexerService.indexRepository(
                repoFolder,
                repoName);

        RepositoryEntity repo =
                new RepositoryEntity();

        repo.setRepoName(
                repoName);

        repo.setRepoUrl(
                repoUrl);

        repo.setIndexedAt(
                LocalDateTime.now());

        repositoryRepository.save(
                repo);

        System.out.println(
                "INDEXED REPOSITORY = "
                        + repoName);
    }

    private String extractRepoName(
            String url) {

        String name =
                url.substring(
                        url.lastIndexOf("/")
                                + 1);

        return name.replace(
                ".git",
                "");
    }
}