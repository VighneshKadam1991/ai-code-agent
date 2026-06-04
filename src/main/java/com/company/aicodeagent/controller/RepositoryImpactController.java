package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.RepositoryImpactResponse;
import com.company.aicodeagent.service.RepositoryImpactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepositoryImpactController {

    private final RepositoryImpactService service;

    public RepositoryImpactController(
            RepositoryImpactService service) {

        this.service = service;
    }

    @GetMapping("/repository-impact")
    public List<RepositoryImpactResponse>
    repositoryImpact(
            @RequestParam String repo) {

        return service.findImpact(repo);
    }
}