package com.company.aicodeagent.service;

import com.company.aicodeagent.dto.*;
import com.company.aicodeagent.entity.FieldReferenceEntity;
import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.entity.MethodReferenceEntity;
import com.company.aicodeagent.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final JavaClassRepository
            javaClassRepository;

    public AiAnalysisService(
            ClaudeService claudeService,
            JavaClassRepository repository,
            GraphTraversalService graphTraversalService,
            ClassDependencyRepository dependencyRepository,
            ApiFlowRepository apiFlowRepository,
            ImpactAnalysisService impactAnalysisService,
            FieldReferenceRepository fieldReferenceRepository,MethodReferenceRepository
                    methodReferenceRepository,JavaClassRepository javaClassRepository) {

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
        this.javaClassRepository =
                javaClassRepository;
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

    public AiAnalysisResponse analyze(
            ChangeAnalysisRequest request) {

        List<CodeChangeImpact> impacts =
                impactAnalysisService
                        .serviceFieldChangeImpact(
                                request);

        String prompt =
                buildPrompt(
                        impacts,
                        request);

        String claudeResponse =
                claudeService.askClaude(
                        prompt);

        System.out.println(
                "CLAUDE RESPONSE = "
                        + claudeResponse);

        ObjectMapper mapper =
                new ObjectMapper();
        try {

            claudeResponse =
                    claudeResponse
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();

            System.out.println(
                    "CLEANED RESPONSE = ");

            System.out.println(
                    claudeResponse);

        AiAnalysisResponse response =
                mapper.readValue(
                        claudeResponse,
                        AiAnalysisResponse.class);

        response.setImpacts(
                impacts);

        return response;
        } catch (Exception ex) {

            System.out.println(
                    "JSON PARSE FAILED");

            System.out.println(
                    claudeResponse);

            AiAnalysisResponse response =
                    new AiAnalysisResponse();

            response.setSummary(
                    claudeResponse);

            response.setImpacts(
                    impacts);

            return response;
        }
    }

    private String buildPrompt(
            List<CodeChangeImpact> impacts,
            ChangeAnalysisRequest request)
    {
        StringBuilder prompt =
                new StringBuilder();

        prompt.append(
                "A field is being renamed.\n\n");

        prompt.append(
                        "Service: ")
                .append(
                        request.getService())
                .append("\n");

        prompt.append(
                        "Entity: ")
                .append(
                        request.getEntity())
                .append("\n");

        prompt.append(
                        "Old Field: ")
                .append(
                        request.getOldField())
                .append("\n");

        prompt.append(
                        "New Field: ")
                .append(
                        request.getNewField())
                .append("\n\n");

        prompt.append(
                "Impacted Files:\n");
        for (CodeChangeImpact impact : impacts) {

            prompt.append(
                            "Repository: ")
                    .append(
                            impact.getRepo())
                    .append("\n");

            prompt.append(
                            "File: ")
                    .append(
                            impact.getFile())
                    .append("\n");

            prompt.append(
                            "Old Code: ")
                    .append(
                            impact.getOldCode())
                    .append("\n");

            prompt.append(
                            "New Code: ")
                    .append(
                            impact.getNewCode())
                    .append("\n\n");
        }

        prompt.append(
                """
        
        Return ONLY valid JSON.
        
        {
          "summary":"",
          "risk":"",
          "migrationPlan":"",
          "deploymentOrder":""
        }
        
        Do not return markdown.
        Do not return explanations.
        Do not use code blocks.
        Do not return any text outside JSON.
        
                """);

        return prompt.toString();

    }

    public AiAnalysisResponse chat(
            String prompt) {

        try {

            String extractionPrompt =
                    """
                    Convert the user request
                    into JSON.
            
                    Return ONLY JSON.
            
                    Field rename example:
            
                    {
                      "changeType":"FIELD_RENAME",
                      "service":"customer-service",
                      "entity":"Customer",
                      "oldField":"lastName",
                      "newField":"surname"
                    }
            
                    Method rename example:
            
                    {
                      "changeType":"METHOD_RENAME",
                      "className":"CustomerClient",
                      "oldMethod":"getCustomer",
                      "newMethod":"fetchCustomer"
                    }
            
                    User Request:
                    """
                            + prompt;

            String extractedJson =
                    claudeService.askClaude(
                            extractionPrompt);

            System.out.println(
                    "CLAUDE RESPONSE = ");

            System.out.println(
                    extractedJson);

            extractedJson =
                    extractedJson
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();

            System.out.println(
                    "CLEANED JSON = ");

            System.out.println(
                    extractedJson);

            ObjectMapper mapper =
                    new ObjectMapper();

            ChangeAnalysisRequest request =
                    mapper.readValue(
                            extractedJson,
                            ChangeAnalysisRequest.class);

            System.out.println(
                    "CHANGE TYPE = "
                            + request.getChangeType());

            if ("METHOD_RENAME".equalsIgnoreCase(
                    request.getChangeType())) {

                MethodChangeRequest methodRequest =
                        new MethodChangeRequest();

                methodRequest.setClassName(
                        request.getClassName());

                methodRequest.setOldMethod(
                        request.getOldMethod());

                methodRequest.setNewMethod(
                        request.getNewMethod());

                List<CodeChangeImpact> impacts =
                        impactAnalysisService
                                .serviceMethodChangeImpact(
                                        methodRequest);


                AiAnalysisResponse response =
                        new AiAnalysisResponse();

                response.setSummary(
                        "Method rename impact analysis");

                response.setImpacts(
                        impacts);

                return response;
            }

            return analyze(
                    request);

        } catch (Exception ex) {

            throw new RuntimeException(
                    ex);
        }
    }
    public List<CodePatch> generatePatch(
            String prompt) {

        try {

            String extractionPrompt =
                    """
                    Convert the user request
                    into JSON.
        
                            Return ONLY raw JSON.
                            
                            Do NOT use markdown.
                            Do NOT use ```json.
                            Do NOT wrap JSON in code fences.
                            
                            Example:
                            
                            {
                              "service":"customer-service",
                              "entity":"Customer",
                              "oldField":"lastName",
                              "newField":"surname"
                            }
        
                    {
                      "service":"",
                      "entity":"",
                      "oldField":"",
                      "newField":""
                    }
        
                    User Request:
                    """
                            + prompt;

            String extractedJson =
                    claudeService.askClaude(
                            extractionPrompt);

            System.out.println(
                    "CLAUDE RESPONSE = ");

            System.out.println(
                    extractedJson);

            extractedJson =
                    extractedJson
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();

            System.out.println(
                    "CLEANED JSON = ");

            System.out.println(
                    extractedJson);

            ObjectMapper mapper =
                    new ObjectMapper();

            ChangeAnalysisRequest request =
                    mapper.readValue(
                            extractedJson,
                            ChangeAnalysisRequest.class);

            AiAnalysisResponse analysis =
                    analyze(
                            request);

            System.out.println(
                    "IMPACTS FOUND = "
                            + analysis.getImpacts().size());
            for (CodeChangeImpact impact :
                    analysis.getImpacts()) {

                System.out.println(
                        "PATCH TARGET = "
                                + impact.getFile());

                System.out.println(
                        "OLD CODE = "
                                + impact.getOldCode());

                System.out.println(
                        "NEW CODE = "
                                + impact.getNewCode());
            }
            List<CodePatch> patches =
                    new ArrayList<>();

            for (CodeChangeImpact impact :
                    analysis.getImpacts()) {

                CodePatch patch =
                        new CodePatch();

                patch.setRepo(
                        impact.getRepo());

                patch.setFile(
                        impact.getFile());

                patch.setOriginalCode(
                        impact.getOldCode());

                patch.setUpdatedCode(
                        impact.getNewCode());

                patches.add(
                        patch);
            }

            return patches;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(
                    ex);
        }


    }
    public List<GeneratedPatch>
    generateFullPatch(
            String prompt) {

        try {

            String extractionPrompt =
                    """
                    Convert the user request
                    into JSON.
            
                    Return ONLY JSON.
            
                    Field rename example:
            
                    {
                      "changeType":"FIELD_RENAME",
                      "service":"customer-service",
                      "entity":"Customer",
                      "oldField":"lastName",
                      "newField":"surname"
                    }
            
                    Method rename example:
            
                    {
                      "changeType":"METHOD_RENAME",
                      "className":"CustomerClient",
                      "oldMethod":"getCustomer",
                      "newMethod":"fetchCustomer"
                    }
            
                    User Request:
                    """
                            + prompt;

            String extractedJson =
                    claudeService.askClaude(
                            extractionPrompt);

            System.out.println(
                    "PATCH CLAUDE RESPONSE = ");

            System.out.println(
                    extractedJson);

            extractedJson =
                    extractedJson
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();

            ObjectMapper mapper =
                    new ObjectMapper();

            ChangeAnalysisRequest request =
                    mapper.readValue(
                            extractedJson,
                            ChangeAnalysisRequest.class);

            AiAnalysisResponse analysis;
            System.out.println(
                    "PATCH CHANGE TYPE = "
                            + request.getChangeType());

            if ("METHOD_RENAME".equalsIgnoreCase(
                    request.getChangeType())) {

                MethodChangeRequest methodRequest =
                        new MethodChangeRequest();

                methodRequest.setClassName(
                        request.getClassName());

                methodRequest.setOldMethod(
                        request.getOldMethod());

                methodRequest.setNewMethod(
                        request.getNewMethod());

                List<CodeChangeImpact> impacts =
                        impactAnalysisService
                                .serviceMethodChangeImpact(
                                        methodRequest);

                analysis =
                        new AiAnalysisResponse();

                analysis.setImpacts(
                        impacts);

                System.out.println(
                        "METHOD IMPACTS FOUND = "
                                + impacts.size());



            } else {

                analysis =
                        analyze(
                                request);
            }

            List<GeneratedPatch> patches =
                    new ArrayList<>();
            System.out.println(
                    "PATCH IMPACTS = "
                            + analysis.getImpacts().size());
            for (CodeChangeImpact impact :
                    analysis.getImpacts()) {

                JavaClassEntity javaClass =
                        javaClassRepository
                                .findByFilePath(
                                        impact.getFile())
                                .orElseThrow();

                String sourceCode =
                        javaClass.getSourceCode();

                String patchPrompt =
                        """
                        Update this Java file.
    
                        Replace:
    
                        """
                                + impact.getOldCode()
                                + """

                    with

                    """
                                + impact.getNewCode()
                                + """

                    Return ONLY the full updated Java file.

                    Source File:

                    """
                                + sourceCode;
                String updatedFile =
                        claudeService.askClaude(
                                patchPrompt);

                updatedFile =
                        updatedFile
                                .replaceAll(
                                        "```[a-zA-Z]*",
                                        "")
                                .replace(
                                        "```",
                                        "")
                                .trim();

                GeneratedPatch patch =
                        new GeneratedPatch();

                patch.setRepo(
                        impact.getRepo());

                patch.setFile(
                        impact.getFile());

                patch.setOriginalFile(
                        sourceCode);

                patch.setUpdatedFile(
                        updatedFile);

                patches.add(
                        patch);
            }

            return patches;

        } catch (Exception ex) {

            ex.printStackTrace();

            throw new RuntimeException(
                    ex);
        }
    }
}