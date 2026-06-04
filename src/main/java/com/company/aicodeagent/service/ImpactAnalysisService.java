package com.company.aicodeagent.service;

import com.company.aicodeagent.controller.SearchController;
import com.company.aicodeagent.dto.*;
import com.company.aicodeagent.entity.*;
import com.company.aicodeagent.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImpactAnalysisService {

    private final SearchService searchService;
    private final FieldReferenceRepository
            fieldReferenceRepository;

    private final MethodReferenceRepository
            methodReferenceRepository;

    private final MethodCallRepository
            methodCallRepository;
    private final VariableTypeRepository
            variableTypeRepository;
    private final ServiceDependencyRepository
            serviceDependencyRepository;

    private final JavaClassRepository
            javaClassRepository;

    public ImpactAnalysisService(SearchService searchService,
                                 FieldReferenceRepository fieldReferenceRepository,
                                 MethodReferenceRepository methodReferenceRepository,
                                 MethodCallRepository methodCallRepository,VariableTypeRepository
                                         variableTypeRepository,ServiceDependencyRepository
                                         serviceDependencyRepository,JavaClassRepository
                                         javaClassRepository)
    {
        this.searchService = searchService;
        this.fieldReferenceRepository =                fieldReferenceRepository;
        this.methodReferenceRepository =                methodReferenceRepository;
        this.methodCallRepository =                methodCallRepository;
        this.variableTypeRepository =
                variableTypeRepository;
        this.serviceDependencyRepository =
                serviceDependencyRepository;
        this.javaClassRepository =
                javaClassRepository;
    }

    public List<ImpactFileResponse>
    filesImpact(String entity) {

        return new ArrayList<>();
    }

    public String analyze(String issue) {

        List<JavaClassEntity> matches =
                searchService.search(issue);

        StringBuilder result =
                new StringBuilder();

        result.append("Impacted Files\n");
        result.append("====================\n\n");
        System.out.println(
                "Matches found = "
                        + matches.size());
        for (JavaClassEntity file : matches) {

            result.append("Class: ")
                    .append(file.getClassName())
                    .append("\n");

            result.append("Path: ")
                    .append(file.getFilePath())
                    .append("\n\n");
        }

        return result.toString();
    }

    public List<ImpactResultEntity>
    analyzeFieldRename(
            String entity,
            String field) {

        List<ImpactResultEntity> results =
                new ArrayList<>();

        var fieldRefs =
                fieldReferenceRepository
                        .findByTargetClassAndFieldName(
                                entity,
                                field.toLowerCase());

        for (var ref : fieldRefs) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "FIELD_RENAME");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    ref.getSourceClass());

            impact.setImpactReason(
                    "Field Reference");

            results.add(
                    impact);
        }

        String normalizedField =
                field.substring(0,1).toUpperCase()
                        + field.substring(1);

        String getter =
                "get" + normalizedField;

        String setter =
                "set" + normalizedField;

      /*  String getter =
                "get"
                        + Character.toUpperCase(
                        field.charAt(0))
                        + field.substring(1);

        String setter =
                "set"
                        + Character.toUpperCase(
                        field.charAt(0))
                        + field.substring(1);*/
        var getterRefs =
                methodReferenceRepository
                        .findByTargetClassAndMethodName(
                                entity,
                                getter);

        for (var ref : getterRefs) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "GETTER_REFERENCE");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    ref.getSourceClass());

            impact.setImpactedMethod(
                    ref.getMethodName());

            impact.setImpactReason(
                    "Getter Reference");

            results.add(
                    impact);
        }

        var setterRefs =
                methodReferenceRepository
                        .findByTargetClassAndMethodName(
                                entity,
                                setter);

        for (var ref : setterRefs) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "SETTER_REFERENCE");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    ref.getSourceClass());

            impact.setImpactedMethod(
                    ref.getMethodName());

            impact.setImpactReason(
                    "Setter Reference");

            results.add(
                    impact);
        }

        var getterCalls =
                methodCallRepository
                        .findByTargetClassAndTargetMethod(
                                entity,
                                getter);

        for (var call : getterCalls) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "METHOD_CALL");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    call.getSourceClass());

            impact.setImpactedMethod(
                    call.getSourceMethod());

            impact.setImpactReason(
                    "Getter Method Call");

            results.add(
                    impact);
        }

        var setterCalls =
                methodCallRepository
                        .findByTargetClassAndTargetMethod(
                                entity,
                                setter);
        for (var call : setterCalls) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "METHOD_CALL");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    call.getSourceClass());

            impact.setImpactedMethod(
                    call.getSourceMethod());

            impact.setImpactReason(
                    "Setter Method Call");

            results.add(
                    impact);
        }
        return results;
    }

    public List<ImpactResultEntity>
    analyzeMethodRename(
            String entity,
            String method) {

        List<ImpactResultEntity> results =
                new ArrayList<>();

        var calls =
                methodCallRepository
                        .findByTargetClassAndTargetMethod(
                                entity,
                                method);

        for (var call : calls) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "METHOD_RENAME");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    call.getSourceClass());

            impact.setImpactedMethod(
                    call.getSourceMethod());

            impact.setImpactReason(
                    "Method Call");

            results.add(
                    impact);
        }

        return results;
    }

    public List<ImpactResultEntity>
    analyzeEntityRename(
            String entity) {
        Set<String> seen =
                new HashSet<>();
        List<ImpactResultEntity> results =
                new ArrayList<>();

        // Variable Types
        var variableTypes =
                variableTypeRepository
                        .findByTypeName(
                                entity);

        for (var variable : variableTypes) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "ENTITY_RENAME");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    variable.getSourceClass());

            impact.setImpactReason(
                    "Variable Type");

            String key =
                    impact.getImpactedClass()
                            + "|"
                            + impact.getImpactReason();

            if (seen.add(key)) {
                results.add(impact);
            }
        }

        // Method References
        var methodRefs =
                methodReferenceRepository
                        .findByTargetClass(
                                entity);

        for (var ref : methodRefs) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "ENTITY_RENAME");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    ref.getSourceClass());

            impact.setImpactedMethod(
                    ref.getMethodName());

            impact.setImpactReason(
                    "Method Reference");

            String key =
                    impact.getImpactedClass()
                            + "|"
                            + impact.getImpactedMethod()
                            + "|METHOD_REF";

            if (seen.add(key)) {
                results.add(impact);
            }
        }

        // Field References
        var fieldRefs =
                fieldReferenceRepository
                        .findByTargetClass(
                                entity);

        for (var ref : fieldRefs) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "ENTITY_RENAME");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    ref.getSourceClass());

            impact.setImpactReason(
                    "Field Reference");

            String key =
                    impact.getImpactedClass()
                            + "|FIELD_REF";

            if (seen.add(key)) {
                results.add(impact);
            }
        }

        // Method Calls
        var calls =
                methodCallRepository
                        .findByTargetClass(
                                entity);

        for (var call : calls) {

            ImpactResultEntity impact =
                    new ImpactResultEntity();

            impact.setChangeType(
                    "ENTITY_RENAME");

            impact.setEntityName(
                    entity);

            impact.setImpactedClass(
                    call.getSourceClass());

            impact.setImpactedMethod(
                    call.getSourceMethod());

            impact.setImpactReason(
                    "Method Call");

            String key =
                    impact.getImpactedClass()
                            + "|"
                            + impact.getImpactedMethod()
                            + "|METHOD_CALL";

            if (seen.add(key)) {
                results.add(impact);
            }
        }

        return results;
    }


    public List<CodeChangeImpact>
    fieldChangeImpact(
            FieldChangeRequest request) {
        Set<String> seen =
                new HashSet<>();
        List<CodeChangeImpact> results =
                new ArrayList<>();

        List<FieldReferenceEntity> refs =
                fieldReferenceRepository
                        .findByTargetClassIgnoreCaseAndFieldNameIgnoreCase(
                                request.getEntity(),
                                request.getOldField());

        for (FieldReferenceEntity ref : refs) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    ref.getRepoName());

            impact.setFile(
                    ref.getFilePath());

            impact.setLine(
                    ref.getLineNumber());

            String oldCode =
                    ref.getCodeSnippet();

            impact.setOldCode(
                    oldCode);

            String oldProperty =
                    normalizePropertyName(
                            request.getOldField());

            String newProperty =
                    normalizePropertyName(
                            request.getNewField());

            String newCode =
                    oldCode.replace(
                            oldProperty,
                            newProperty);

            impact.setNewCode(
                    newCode);

            impact.setImpactType(
                    "FIELD_GETTER");

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (!impact.getOldCode()
                    .equals(
                            impact.getNewCode())
                    && seen.add(key)) {

                results.add(
                        impact);
            }
        }

        String oldProperty =
                normalizePropertyName(
                        request.getOldField());

        String getter =
                "get" + oldProperty;

        String setter =
                "set" + oldProperty;

        var getterRefs =
                methodReferenceRepository
                        .findByTargetClassAndMethodName(
                                request.getEntity(),
                                getter);

        for (var ref : getterRefs) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    ref.getRepoName());

            impact.setFile(
                    ref.getFilePath());

            impact.setLine(
                    ref.getLineNumber());

            impact.setOldCode(
                    ref.getCodeSnippet());

            impact.setNewCode(
                    buildReplacement(
                            ref.getCodeSnippet(),
                            request.getOldField(),
                            request.getNewField()));

            impact.setImpactType(
                    "GETTER_REFERENCE");

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (!impact.getOldCode()
                    .equals(
                            impact.getNewCode())
                    && seen.add(key)) {

                results.add(
                        impact);
            }
        }

        var setterRefs =
                methodReferenceRepository
                        .findByTargetClassAndMethodName(
                                request.getEntity(),
                                setter);

        for (var ref : setterRefs) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    ref.getRepoName());

            impact.setFile(
                    ref.getFilePath());

            impact.setLine(
                    ref.getLineNumber());

            impact.setOldCode(
                    ref.getCodeSnippet());

            impact.setNewCode(
                    buildReplacement(
                            ref.getCodeSnippet(),
                            request.getOldField(),
                            request.getNewField()));

            impact.setImpactType(
                    "SETTER_REFERENCE");

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (!impact.getOldCode()
                    .equals(
                            impact.getNewCode())
                    && seen.add(key)) {

                results.add(
                        impact);
            }
        }
        var getterCalls =
                methodCallRepository
                        .findByTargetClassAndTargetMethod(
                                request.getEntity(),
                                getter);


        for (var call : getterCalls) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    call.getRepoName());

            impact.setFile(
                    call.getFilePath());

            impact.setLine(
                    call.getLineNumber());

            impact.setOldCode(
                    call.getCodeSnippet());

            impact.setNewCode(
                    buildReplacement(
                            call.getCodeSnippet(),
                            request.getOldField(),
                            request.getNewField()));

            impact.setImpactType(
                    "GETTER_METHOD_CALL");

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (!impact.getOldCode()
                    .equals(
                            impact.getNewCode())
                    && seen.add(key)) {

                results.add(
                        impact);
            }
        }

        var setterCalls =
                methodCallRepository
                        .findByTargetClassAndTargetMethod(
                                request.getEntity(),
                                setter);

        for (var call : setterCalls) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    call.getRepoName());

            impact.setFile(
                    call.getFilePath());

            impact.setLine(
                    call.getLineNumber());

            impact.setOldCode(
                    call.getCodeSnippet());

            impact.setNewCode(
                    buildReplacement(
                            call.getCodeSnippet(),
                            request.getOldField(),
                            request.getNewField()));

            impact.setImpactType(
                    "SETTER_METHOD_CALL");

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (!impact.getOldCode()
                    .equals(
                            impact.getNewCode())
                    && seen.add(key)) {

                results.add(
                        impact);
            }
        }



        return results;
    }

    private String normalizePropertyName(
            String fieldName) {

        if (fieldName == null
                || fieldName.isBlank()) {

            return fieldName;
        }

        if ("lastname".equalsIgnoreCase(fieldName)) {
            return "LastName";
        }

        if ("firstname".equalsIgnoreCase(fieldName)) {
            return "FirstName";
        }

        return Character.toUpperCase(
                fieldName.charAt(0))
                + fieldName.substring(1);
    }

    private String buildReplacement(
            String code,
            String oldField,
            String newField) {

        String oldProperty =
                normalizePropertyName(
                        oldField);

        String newProperty =
                normalizePropertyName(
                        newField);

        return code.replace(
                oldProperty,
                newProperty);
    }


    public List<CodeChangeImpact>
    entityChangeImpact(
            EntityChangeRequest request) {

        Set<String> seen =
                new HashSet<>();

        List<CodeChangeImpact> results =
                new ArrayList<>();

        // =====================================================
        // VARIABLE TYPES
        // =====================================================

        var variables =
                variableTypeRepository
                        .findByTypeName(
                                request.getOldEntity());

        for (var variable : variables) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    variable.getRepoName());

            impact.setFile(
                    variable.getFilePath());

            impact.setLine(
                    variable.getLineNumber());

            impact.setOldCode(
                    variable.getTypeName());

            impact.setNewCode(
                    request.getNewEntity());

            impact.setImpactType(
                    "VARIABLE_TYPE");

            impact.setRequiresCodeChange(true);

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (!impact.getOldCode()
                    .equals(
                            impact.getNewCode())
                    && seen.add(key)) {

                results.add(
                        impact);
            }
        }

        // =====================================================
        // METHOD REFERENCES
        // =====================================================

        var methodRefs =
                methodReferenceRepository
                        .findByTargetClass(
                                request.getOldEntity());

        for (var ref : methodRefs) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    ref.getRepoName());

            impact.setFile(
                    ref.getFilePath());

            impact.setLine(
                    ref.getLineNumber());

            impact.setOldCode(
                    ref.getCodeSnippet());

            impact.setNewCode(ref.getCodeSnippet());

            impact.setImpactType(
                    "METHOD_REFERENCE");

            impact.setRequiresCodeChange(false);

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (seen.add(key)) {

                results.add(
                        impact);
            }
        }

        // =====================================================
        // FIELD REFERENCES
        // =====================================================

        var fieldRefs =
                fieldReferenceRepository
                        .findByTargetClass(
                                request.getOldEntity());

        for (var ref : fieldRefs) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    ref.getRepoName());

            impact.setFile(
                    ref.getFilePath());

            impact.setLine(
                    ref.getLineNumber());

            impact.setOldCode(
                    ref.getCodeSnippet());

            impact.setNewCode(ref.getCodeSnippet());

            impact.setImpactType(
                    "FIELD_REFERENCE");

            impact.setRequiresCodeChange(false);

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (seen.add(key)) {

                results.add(
                        impact);
            }
        }

        // =====================================================
        // METHOD CALLS
        // =====================================================

        var calls =
                methodCallRepository
                        .findByTargetClass(
                                request.getOldEntity());

        for (var call : calls) {

            CodeChangeImpact impact =
                    new CodeChangeImpact();

            impact.setRepo(
                    call.getRepoName());

            impact.setFile(
                    call.getFilePath());

            impact.setLine(
                    call.getLineNumber());

            impact.setOldCode(
                    call.getCodeSnippet());

            impact.setNewCode(call.getCodeSnippet());

            impact.setImpactType(
                    "METHOD_CALL");

            impact.setRequiresCodeChange(false);

            String key =
                    impact.getFile()
                            + "|"
                            + impact.getLine()
                            + "|"
                            + impact.getImpactType();

            if (seen.add(key)) {

                results.add(
                        impact);
            }
        }

        return results;
    }


    public List<CodeChangeImpact>
    entityChangeImpactRequired(
            EntityChangeRequest request) {

        return entityChangeImpact(request)
                .stream()
                .filter(
                        CodeChangeImpact::isRequiresCodeChange)
                .toList();
    }

    public List<ServiceDependencyEntity>
    serviceImpact(
            String service) {

        return serviceDependencyRepository
                .findByTargetService(
                        service);
    }


    public List<CodeChangeImpact>
    serviceImpactFiles(
            String service) {
        Set<String> seen =
                new HashSet<>();
        List<ServiceDependencyEntity> deps =
                serviceDependencyRepository
                        .findByTargetService(
                                service);

        List<CodeChangeImpact> results =
                new ArrayList<>();

        for (ServiceDependencyEntity dep : deps) {

            List<JavaClassEntity> classes =
                    javaClassRepository.findByRepoName(
                            dep.getRepoName());

            for (JavaClassEntity clazz : classes) {

                CodeChangeImpact impact =
                        new CodeChangeImpact();

                impact.setRepo(
                        dep.getRepoName());

                impact.setFile(
                        clazz.getFilePath());

                impact.setOldCode(
                        "Depends on service: "
                                + dep.getTargetService());

                impact.setNewCode(
                        "Review API contract changes");

                impact.setImpactType(
                        "SERVICE_DEPENDENCY");

                String key =
                        clazz.getFilePath();

                if (seen.add(key)) {

                    results.add(
                            impact);
                }
            }
        }

        return results;
    }

    public List<ServiceMethodImpact>
    serviceImpactMethods(
            String service) {

        List<ServiceMethodImpact> results =
                new ArrayList<>();

        List<ServiceDependencyEntity> deps =
                serviceDependencyRepository
                        .findByTargetService(
                                service);

        for (ServiceDependencyEntity dep : deps) {

            List<MethodCallEntity> calls =
                    methodCallRepository
                            .findByRepoName(
                                    dep.getRepoName());

            for (MethodCallEntity call : calls) {

                ServiceMethodImpact impact =
                        new ServiceMethodImpact();

                impact.setRepo(
                        call.getRepoName());

                impact.setFile(
                        call.getFilePath());

                impact.setClassName(
                        call.getSourceClass());

                impact.setMethodName(
                        call.getSourceMethod());

                impact.setImpactType(
                        "SERVICE_METHOD");

                results.add(
                        impact);
            }
        }

        return results;
    }
    public List<CodeChangeImpact>
    serviceApiChangeImpact(
            ServiceApiChangeRequest request) {

        Set<String> seen =
                new HashSet<>();

        List<CodeChangeImpact> results =
                new ArrayList<>();

        List<ServiceDependencyEntity> deps =
                serviceDependencyRepository
                        .findByTargetService(
                                request.getService());

        for (ServiceDependencyEntity dep : deps) {

            List<MethodCallEntity> calls =
                    methodCallRepository
                            .findByRepoName(
                                    dep.getRepoName());

            for (MethodCallEntity call : calls) {

                CodeChangeImpact impact =
                        new CodeChangeImpact();

                impact.setRepo(
                        call.getRepoName());

                impact.setFile(
                        call.getFilePath());

                impact.setLine(
                        call.getLineNumber());

                impact.setOldCode(
                        request.getOldEndpoint());

                impact.setNewCode(
                        request.getNewEndpoint());

                impact.setImpactType(
                        "SERVICE_API_CHANGE");

                impact.setRequiresCodeChange(
                        true);

                String key =
                        impact.getFile()
                                + "|"
                                + impact.getLine();

                if (seen.add(key)) {

                    results.add(
                            impact);
                }
            }
        }

        return results;
    }
}
