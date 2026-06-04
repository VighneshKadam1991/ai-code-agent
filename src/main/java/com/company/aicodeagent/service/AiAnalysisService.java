package com.company.aicodeagent.service;

import com.company.aicodeagent.dto.ImpactFileResponse;
import com.company.aicodeagent.entity.FieldReferenceEntity;
import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.entity.MethodReferenceEntity;
import com.company.aicodeagent.repository.*;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AiAnalysisService {

    private final ClaudeService claudeService;

    private final JavaClassRepository repository;

    private final GraphTraversalService graphTraversalService;

    private final ClassDependencyRepository dependencyRepository;

    private final ApiFlowRepository apiFlowRepository;

    private final FieldReferenceRepository
            fieldReferenceRepository;

    private final ImpactAnalysisService
            impactAnalysisService;

    private final MethodReferenceRepository
            methodReferenceRepository;



    public AiAnalysisService(
            ClaudeService claudeService,
            JavaClassRepository repository,
            GraphTraversalService graphTraversalService,
            ClassDependencyRepository dependencyRepository,
            ApiFlowRepository apiFlowRepository,
            ImpactAnalysisService impactAnalysisService,
            FieldReferenceRepository fieldReferenceRepository,MethodReferenceRepository
                    methodReferenceRepository) {

        this.claudeService = claudeService;
        this.repository = repository;
        this.graphTraversalService = graphTraversalService;
        this.dependencyRepository = dependencyRepository;
        this.apiFlowRepository = apiFlowRepository;
        this.impactAnalysisService =
                impactAnalysisService;
        this.fieldReferenceRepository =
                fieldReferenceRepository;
        this.methodReferenceRepository =
                methodReferenceRepository;
    }

    public String analyze(String issue) {

        Set<String> rootClasses =
                extractClasses(issue);

        System.out.println(
                "ROOT CLASSES = "
                        + rootClasses);

        List<FieldReferenceEntity> fieldRefs =
                new ArrayList<>();

        Map<String, JavaClassEntity> uniqueClasses =
                new HashMap<>();

        List<MethodReferenceEntity> methodRefs =
                new ArrayList<>();

        for (String rootClass
                : rootClasses) {


            List<MethodReferenceEntity> currentMethodRefs =
                    methodReferenceRepository
                            .findByTargetClassIgnoreCase(
                                    rootClass);

            methodRefs.addAll(
                    currentMethodRefs);

            List<ImpactFileResponse> impactedFiles =
                    impactAnalysisService
                            .filesImpact(
                                    rootClass);

            System.out.println(
                    "IMPACT FILES = "
                            + impactedFiles.size());

            for (ImpactFileResponse file
                    : impactedFiles) {

                System.out.println(
                        "FILE SENT = "
                                + file.getClassName());

                repository
                        .findByClassNameIgnoreCase(
                                file.getClassName())
                        .forEach(entity ->
                                uniqueClasses.put(
                                        entity.getClassName(),
                                        entity));
            }

            for (MethodReferenceEntity ref
                    : currentMethodRefs) {

                repository
                        .findByClassNameIgnoreCase(
                                ref.getSourceClass())
                        .forEach(entity ->
                                uniqueClasses.put(
                                        entity.getClassName(),
                                        entity));
            }


            System.out.println(
                    "FILES AFTER METHOD IMPACT = "
                            + uniqueClasses.size());

            for (JavaClassEntity clazz
                    : uniqueClasses.values()) {

                System.out.println(
                        "IMPACT FILE = "
                                + clazz.getClassName());
            }
            List<FieldReferenceEntity> currentFieldRefs =
                    fieldReferenceRepository
                            .findByTargetClassIgnoreCase(
                                    rootClass);

            fieldRefs.addAll(
                    currentFieldRefs);

            for (FieldReferenceEntity ref
                    : currentFieldRefs) {

                repository
                        .findByClassNameIgnoreCase(
                                ref.getSourceClass())
                        .forEach(entity ->
                                uniqueClasses.put(
                                        entity.getClassName(),
                                        entity));
            }


        }

        System.out.println(
                "FIELD REFS = "
                        + fieldRefs.size());

        for (FieldReferenceEntity ref
                : fieldRefs) {

            System.out.println(
                    ref.getSourceClass()
                            + " -> "
                            + ref.getTargetClass()
                            + "."
                            + ref.getFieldName());
        }

        System.out.println(
                "METHOD REFS = "
                        + methodRefs.size());

        for (MethodReferenceEntity ref
                : methodRefs) {

            System.out.println(
                    ref.getSourceClass()
                            + " -> "
                            + ref.getTargetClass()
                            + "."
                            + ref.getMethodName());
        }

        List<JavaClassEntity> classes =
                new ArrayList<>(
                        uniqueClasses.values());

        System.out.println(
                "FILES SENT TO CLAUDE = "
                        + classes.size());

        for (JavaClassEntity clazz
                : classes) {

            System.out.println(
                    "FILE SENT = "
                            + clazz.getClassName());
        }

        // keep the rest of your method unchanged

        classes.forEach(c ->
                System.out.println(
                        " -> "
                                + c.getClassName()));

        StringBuilder prompt =
                new StringBuilder();

        prompt.append(
                "You are a senior Java microservice architect.\n\n");

        prompt.append(
                "Issue:\n");

        prompt.append(issue)
                .append("\n\n");

        prompt.append(
                "Root Classes:\n");

        rootClasses.forEach(clazz ->
                prompt.append(clazz)
                        .append("\n"));

        prompt.append("\n");

        prompt.append(
                "Impacted Files:\n");

        classes.forEach(clazz ->
                prompt.append(
                                clazz.getClassName())
                        .append("\n"));

        prompt.append("\n");

        prompt.append(
                "Field References:\n");

        for (FieldReferenceEntity ref
                : fieldRefs) {

            prompt.append(
                            ref.getSourceClass())
                    .append(" -> ")
                    .append(ref.getTargetClass())
                    .append(".")
                    .append(ref.getFieldName())
                    .append("\n");
        }

        prompt.append("\n");

        prompt.append(
                "Method References:\n");

        for (MethodReferenceEntity ref
                : methodRefs) {

            prompt.append(
                            ref.getSourceClass())
                    .append(" -> ")
                    .append(ref.getTargetClass())
                    .append(".")
                    .append(ref.getMethodName())
                    .append("\n");
        }

        prompt.append("\n");

        prompt.append(
                "IMPORTANT:\n");

        prompt.append(
                "When recommending code changes, prioritize only the files listed below.\n");

        prompt.append(
                "Do not invent additional files unless they are clearly required.\n\n");

        classes.forEach(clazz ->
                prompt.append("- ")
                        .append(clazz.getClassName())
                        .append(" : ")
                        .append(clazz.getFilePath())
                        .append("\n"));

        prompt.append("\n");

        prompt.append(
                "Return:\n");

        prompt.append(
                "1. Impacted files (ONLY from the impacted files list above)\n");

        prompt.append(
                "2. Exact code changes\n");

        prompt.append(
                "3. Database changes\n");

        prompt.append(
                "4. Risks\n\n");

        for (JavaClassEntity clazz
                : classes) {

            if (clazz.getSourceCode()
                    == null) {

                continue;
            }

            prompt.append("FILE: ")
                    .append(
                            clazz.getFilePath())
                    .append("\n");

            prompt.append("CLASS: ")
                    .append(
                            clazz.getClassName())
                    .append("\n\n");

            prompt.append(
                            clazz.getSourceCode())
                    .append("\n\n");

            prompt.append(
                    "====================================\n\n");
        }

        return claudeService.askClaude(
                prompt.toString());
    }

    private String extractKeyword(
            String issue) {

        List<JavaClassEntity> classes =
                repository.findAll();

        for (JavaClassEntity clazz
                : classes) {

            if (clazz.getClassName()
                    == null) {

                continue;
            }

            if (issue.toLowerCase()
                    .contains(
                            clazz.getClassName()
                                    .toLowerCase())) {

                System.out.println(
                        "MATCHED ROOT CLASS = "
                                + clazz.getClassName());

                return clazz.getClassName();
            }
        }

        System.out.println(
                "NO ROOT CLASS FOUND. USING ISSUE TEXT.");

        return issue;
    }


    private Set<String> extractClasses(
            String issue) {

        Set<String> classes =
                new HashSet<>();

        List<JavaClassEntity> allClasses =
                repository.findAll();

        String lower =
                issue.toLowerCase();

        for (JavaClassEntity clazz
                : allClasses) {

            if (clazz.getClassName()
                    == null) {

                continue;
            }

            if (lower.contains(
                    clazz.getClassName()
                            .toLowerCase())) {

                classes.add(
                        clazz.getClassName());
            }
        }

        return classes;
    }


}