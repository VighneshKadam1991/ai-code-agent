package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.CloneRepositoryRequest;
import com.company.aicodeagent.service.GitCloneService;
import com.company.aicodeagent.service.JavaIndexerService;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final GitCloneService gitCloneService;
    private final JavaIndexerService indexerService;

    public RepositoryController(GitCloneService gitCloneService,
                                JavaIndexerService indexerService) {
        this.gitCloneService = gitCloneService;
        this.indexerService = indexerService;
    }

    @PostMapping("/index")
    public String indexRepository(
            @RequestBody CloneRepositoryRequest request)
            throws Exception {

        File repo =
                gitCloneService.cloneRepository(
                        request.getRepoUrl());

        int count =
                indexerService.indexRepository(
                        repo,
                        repo.getName());

        return "Indexed classes: " + count;
    }
}
