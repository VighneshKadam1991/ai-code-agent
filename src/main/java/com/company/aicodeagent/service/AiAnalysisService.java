package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.repository.JavaClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AiAnalysisService {

    private final ClaudeService claudeService;

    private final JavaClassRepository repository;

    private final GraphTraversalService graphTraversalService;

    public AiAnalysisService(
            ClaudeService claudeService,
            JavaClassRepository repository,
            GraphTraversalService graphTraversalService) {

        this.claudeService = claudeService;
        this.repository = repository;
        this.graphTraversalService =
                graphTraversalService;
    }

    public String analyze(String issue) {

        String keyword = extractKeyword(issue);

        Set<String> impactedClasses =
                graphTraversalService
                        .findImpactedClasses(
                                keyword);

        System.out.println(
                "IMPACTED CLASSES = "
                        + impactedClasses);

        List<JavaClassEntity> classes =
                repository.findAll()
                        .stream()
                        .filter(c ->
                                impactedClasses.contains(
                                        c.getClassName()))
                        .toList();

        System.out.println(
                "FILES SENT TO CLAUDE = "
                        + classes.size());

        StringBuilder prompt =
                new StringBuilder();

        prompt.append(
                "You are a senior Java microservice architect.\n\n");

        prompt.append(
                "Issue:\n");

        prompt.append(issue)
                .append("\n\n");

        prompt.append(
                "Return:\n");

        prompt.append(
                "1. Impacted files\n");

        prompt.append(
                "2. Exact code changes\n");

        prompt.append(
                "3. Database changes\n");

        prompt.append(
                "4. Risks\n\n");

        for (JavaClassEntity clazz : classes) {

            if (clazz.getSourceCode() == null) {
                continue;
            }

            prompt.append("FILE: ")
                    .append(clazz.getFilePath())
                    .append("\n");

            prompt.append("CLASS: ")
                    .append(clazz.getClassName())
                    .append("\n\n");

            prompt.append(clazz.getSourceCode())
                    .append("\n\n");

            prompt.append(
                    "=================================\n\n");
        }

        return claudeService.askClaude(
                prompt.toString());
    }

    private String extractKeyword(
            String issue) {

        String lower =
                issue.toLowerCase();

        if (lower.contains("owner")) {
            return "Owner";
        }

        if (lower.contains("pet")) {
            return "Pet";
        }

        if (lower.contains("visit")) {
            return "Visit";
        }

        return issue;
    }
}