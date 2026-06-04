package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.*;
import com.company.aicodeagent.entity.*;
import com.company.aicodeagent.repository.*;
import com.company.aicodeagent.service.ImpactAnalysisService;
import com.company.aicodeagent.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final ApiEndpointRepository
            apiEndpointRepository;
    private final ApiFlowRepository apiFlowRepository;
    private final ClassDependencyRepository
            dependencyRepository;
    private final JavaClassRepository
            javaClassRepository;
    private final FieldReferenceRepository fieldReferenceRepository;

    private final MethodReferenceRepository methodReferenceRepository;
    private final MethodCallRepository methodCallRepository;
    private final ImpactAnalysisService
            impactAnalysisService;
    public SearchController(SearchService searchService, ApiEndpointRepository apiEndpointRepository,
                            ApiFlowRepository apiFlowRepository, ClassDependencyRepository dependencyRepository,
                             JavaClassRepository  javaClassRepository,FieldReferenceRepository fieldReferenceRepository,
                            MethodReferenceRepository methodReferenceRepository,MethodCallRepository methodCallRepository,
                            ImpactAnalysisService
                                    impactAnalysisService) {
        this.searchService = searchService;
        this.apiEndpointRepository = apiEndpointRepository;
        this.apiFlowRepository =
                apiFlowRepository;
        this.dependencyRepository = dependencyRepository;
        this.javaClassRepository =
                javaClassRepository;
        this.fieldReferenceRepository =
                fieldReferenceRepository;
        this.methodReferenceRepository =
                methodReferenceRepository;
        this.methodCallRepository =
                methodCallRepository;
        this.impactAnalysisService =
                impactAnalysisService;
    }

    @GetMapping
    public List<JavaClassEntity> search(
            @RequestParam String keyword) {

        return searchService.search(keyword);
    }


    @GetMapping("/apis")
    public List<ApiSearchResponse>
    searchApis(
            @RequestParam String keyword) {

        return apiEndpointRepository
                .findByEndpointContainingIgnoreCase(
                        keyword)
                .stream()
                .map(api ->
                        new ApiSearchResponse(
                                api.getRepoName(),
                                api.getControllerClass(),
                                api.getEndpoint(),
                                api.getHttpMethod()))
                .toList();
    }


    @GetMapping("/flows")
    public List<ApiFlowEntity>
    searchFlows(
            @RequestParam String keyword) {

        return apiFlowRepository
                .findByEndpointContainingIgnoreCase(
                        keyword);
    }


    @GetMapping("/impact")
    public List<ApiImpactResponse>
    impact(
            @RequestParam String keyword) {
        Set<String> seen =
                new HashSet<>();
        List<ApiImpactResponse> result =
                new ArrayList<>();

        List<ApiFlowEntity> flows =
                apiFlowRepository
                        .findByEndpointContainingIgnoreCase(
                                keyword);

        System.out.println(
                "FLOW COUNT = "
                        + flows.size());

        for (ApiFlowEntity flow : flows) {
            System.out.println(
                    "FLOW = "
                            + flow.getEndpoint()
                            + " -> "
                            + flow.getTargetClass());
            List<ClassDependencyEntity>
                    repositoryDependencies =
                    dependencyRepository
                            .findBySourceClassIgnoreCase(
                                    flow.getTargetClass());
            System.out.println(
                    "DEPENDENCIES FOUND = "
                            + repositoryDependencies.size());
            System.out.println(
                    "FLOW TARGET = ["
                            + flow.getTargetClass()
                            + "]");

            for (ClassDependencyEntity dep
                    : repositoryDependencies) {

                String key =
                        flow.getHttpMethod()
                                + "|"
                                + flow.getEndpoint()
                                + "|"
                                + flow.getTargetClass()
                                + "|"
                                + dep.getTargetClass();

                if (seen.add(key)) {

                    result.add(
                            new ApiImpactResponse(
                                    flow.getEndpoint(),
                                    flow.getHttpMethod(),
                                    flow.getControllerClass(),
                                    flow.getTargetClass(),
                                    dep.getTargetClass()));
                }
            }
        }

        return result;
    }


    @GetMapping("/files-impact")
    public List<ImpactFileResponse>
    filesImpact(
            @RequestParam String entity) {

        Set<String> seen = new HashSet<>();

        List<ImpactFileResponse> result =
                new ArrayList<>();

        // STEP 1
        List<JavaClassEntity> entities =
                javaClassRepository
                        .findByClassNameIgnoreCase(
                                entity);

        for (JavaClassEntity cls : entities) {


            String key =
                    cls.getClassName()
                            + "|Entity";

            if (seen.add(key)) {

                result.add(
                        new ImpactFileResponse(
                                cls.getClassName(),
                                cls.getFilePath(),
                                "Entity"));
            }
        }

        // STEP 2
        List<ClassDependencyEntity> repos =
                dependencyRepository
                        .findByTargetClassIgnoreCase(
                                entity);

        for (ClassDependencyEntity repo : repos) {

            List<JavaClassEntity> repoFiles =
                    javaClassRepository
                            .findByClassNameIgnoreCase(
                                    repo.getSourceClass());

            for (JavaClassEntity file : repoFiles) {

                String key =
                        file.getClassName()
                                + "|Repository";

                if (seen.add(key)) {

                    result.add(
                            new ImpactFileResponse(
                                    file.getClassName(),
                                    file.getFilePath(),
                                    "Repository"));
                }


            }

            // STEP 3
            List<ApiFlowEntity> flows =
                    apiFlowRepository.findAll();

            for (ApiFlowEntity flow : flows) {

                if (!flow.getTargetClass()
                        .equalsIgnoreCase(
                                repo.getSourceClass())) {

                    continue;
                }

                List<JavaClassEntity>
                        controllers =
                        javaClassRepository
                                .findByClassNameIgnoreCase(
                                        flow.getControllerClass());

                for (JavaClassEntity controller :
                        controllers) {

                    String key =
                            controller.getClassName()
                                    + "|Controller";

                    if (seen.add(key)) {

                        result.add(
                                new ImpactFileResponse(
                                        controller.getClassName(),
                                        controller.getFilePath(),
                                        "Controller"));
                    }

                }
            }
        }

        return result;
    }


    @GetMapping("/field-impact")
    public List<FieldImpactResponse>
    fieldImpact(
            @RequestParam String entity,
            @RequestParam String field) {

        return fieldReferenceRepository
                .findByTargetClassIgnoreCaseAndFieldNameIgnoreCase(
                        entity,
                        field)
                .stream()
                .map(ref ->
                        new FieldImpactResponse(
                                ref.getSourceClass(),
                                ref.getFieldName()))
                .toList();
    }


    @GetMapping("/method-impact")
    public List<MethodImpactResponse>
    methodImpact(
            @RequestParam String entity,
            @RequestParam String method) {

        return methodReferenceRepository
                .findByTargetClassIgnoreCaseAndMethodNameIgnoreCase(
                        entity,
                        method)
                .stream()
                .map(ref ->
                        new MethodImpactResponse(
                                ref.getSourceClass(),
                                ref.getMethodName()))
                .toList();
    }

    @GetMapping(
            "/method-call-impact")
    public List<MethodCallImpactResponse>
    methodCallImpact(
            @RequestParam String entity,
            @RequestParam String method) {

        List<MethodCallEntity> calls =
                methodCallRepository
                        .findByTargetClassIgnoreCaseAndTargetMethodIgnoreCase(
                                entity,
                                method);

        return calls.stream()
                .map(call -> {

                    MethodCallImpactResponse response =
                            new MethodCallImpactResponse();

                    response.setClassName(
                            call.getSourceClass());

                    response.setMethodName(
                            call.getTargetMethod());

                    return response;
                })
                .toList();

    }

    @PostMapping("/entity-rename")
    public List<ImpactResultEntity>
    analyzeEntityRename(
            @RequestBody
            EntityRenameRequest request) {

        return impactAnalysisService
                .analyzeEntityRename(
                        request.getEntity());
    }



}
