package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.*;
import com.company.aicodeagent.parser.ClassMetadata;
import com.company.aicodeagent.repository.*;
import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.company.aicodeagent.parser.ApiEndpointMetadata;
import com.company.aicodeagent.parser.ServiceDependencyMetadata;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JavaIndexerService {

    private final JavaClassRepository repository;

    private final JavaParserService parserService;

    private final ClassDependencyRepository dependencyRepository;
    private final ApiFlowRepository
            apiFlowRepository;
   private final ApiEndpointRepository apiEndpointRepository;
    private final ServiceDependencyRepository
            serviceDependencyRepository;
    private final FieldReferenceRepository
            fieldReferenceRepository;
    private final MethodReferenceRepository
            methodReferenceRepository;

    private final VariableTypeRepository
            variableTypeRepository;
    private final MethodCallRepository
            methodCallRepository;

    private final RepositoryDependencyRepository
            repositoryDependencyRepository;


    private static final Set<String> IGNORED_TYPES =
            Set.of(
                    "String",
                    "Integer",
                    "Long",
                    "Boolean",
                    "Double",
                    "Float",
                    "Short",
                    "Byte",
                    "Character",

                    "int",
                    "long",
                    "double",
                    "float",
                    "short",
                    "byte",
                    "boolean",
                    "char",

                    "LocalDate",
                    "LocalDateTime",
                    "Date",

                    "List",
                    "Set",
                    "Map",
                    "Optional",

                    "Field",
                    "Parameter",

                    "JpaRepository",
                    "Repository",
                    "CrudRepository",

                    "Pageable",
                    "Page",

                    "MockMvc",

                    "Validator",
                    "Errors",
                    "Formatter",

                    "Logger",
                    "Log",

                    "Serializable",

                    "ApplicationListener",
                    "ApplicationPreparedEvent",
                    "ConfigurableEnvironment",

                    "RestTemplateBuilder",

                    "MySQLContainer"
            );

    public JavaIndexerService(
            JavaClassRepository repository,
            JavaParserService parserService,
            ClassDependencyRepository dependencyRepository,
            ApiEndpointRepository apiEndpointRepository,
            ServiceDependencyRepository serviceDependencyRepository,
            ApiFlowRepository apiFlowRepository,
            FieldReferenceRepository fieldReferenceRepository,
            MethodReferenceRepository
                    methodReferenceRepository,VariableTypeRepository
                    variableTypeRepository,
            MethodCallRepository
                    methodCallRepository,RepositoryDependencyRepository
                    repositoryDependencyRepository) {

        this.repository = repository;
        this.parserService = parserService;
        this.dependencyRepository = dependencyRepository;
        this.apiEndpointRepository = apiEndpointRepository;
        this.serviceDependencyRepository =                serviceDependencyRepository;
        this.apiFlowRepository =                apiFlowRepository;
        this.fieldReferenceRepository =                fieldReferenceRepository;
        this.methodReferenceRepository =                methodReferenceRepository;
        this.variableTypeRepository =                variableTypeRepository;
        this.methodCallRepository =                methodCallRepository;
        this.repositoryDependencyRepository =                repositoryDependencyRepository;
    }

    public int indexRepository(
            File repoFolder,
            String repoName)
            throws Exception {

        List<ClassMetadata> classes =
                parserService.parseRepository(
                        repoFolder);

        int savedClasses = 0;
        extractPomDependencies(
                repoName,
                repoFolder.toPath());
        for (ClassMetadata metadata : classes) {

            if (metadata.getClassName() == null
                    || metadata.getClassName().isBlank()) {

                System.out.println(
                        "SKIPPING FILE WITHOUT CLASS: "
                                + metadata.getFilePath());

                continue;
            }

            JavaClassEntity entity =
                    new JavaClassEntity();

            entity.setRepoName(repoName);

            entity.setClassName(
                    metadata.getClassName());

            entity.setPackageName(
                    metadata.getPackageName());

            entity.setFields(
                    String.join(
                            ",",
                            metadata.getFields()));

            entity.setMethods(
                    String.join(
                            ",",
                            metadata.getMethods()));

            entity.setImports(
                    String.join(
                            ",",
                            metadata.getImports()));

            entity.setAnnotations(
                    String.join(
                            ",",
                            metadata.getAnnotations()));

            entity.setFilePath(
                    metadata.getFilePath());

            entity.setSourceCode(
                    metadata.getSourceCode());

            repository.save(entity);

            savedClasses++;

            System.out.println(
                    "CLASS = "
                            + metadata.getClassName());

            System.out.println(
                    "DEPENDENCIES = "
                            + metadata.getDependencies());
            System.out.println(
                    "ENDPOINT COUNT = "
                            + metadata.getEndpoints().size());
            System.out.println(
                    "DEPENDENCY COUNT = "
                            + metadata.getDependencies().size());
            for (ApiEndpointMetadata endpoint :
                    metadata.getEndpoints()) {

                System.out.println(
                        "PROCESSING ENDPOINT = "
                                + endpoint.getEndpoint());

                ApiEndpointEntity apiEntity =
                        new ApiEndpointEntity();

                apiEntity.setRepoName(
                        repoName);

                apiEntity.setControllerClass(
                        endpoint.getControllerClass());

                apiEntity.setEndpoint(
                        endpoint.getEndpoint());

                apiEntity.setHttpMethod(
                        endpoint.getHttpMethod());

                apiEndpointRepository.save(
                        apiEntity);

                Set<String> uniqueDependencies =
                        new HashSet<>(
                                metadata.getDependencies());

                for (String dependency :
                        uniqueDependencies) {

                    if (dependency == null
                            || dependency.isBlank()) {
                        continue;
                    }

                    dependency =
                            dependency.trim();

                    if (IGNORED_TYPES.contains(
                            dependency)) {
                        continue;
                    }

                    if (dependency.equals(
                            metadata.getClassName())) {
                        continue;
                    }

                    ApiFlowEntity flow =
                            new ApiFlowEntity();

                    flow.setRepoName(
                            repoName);

                    flow.setEndpoint(
                            endpoint.getEndpoint());

                    flow.setControllerClass(
                            endpoint.getControllerClass());

                    flow.setTargetClass(
                            dependency);

                    flow.setHttpMethod(
                            endpoint.getHttpMethod());

                    apiFlowRepository.save(
                            flow);

                    System.out.println(
                            "API FLOW SAVED: "
                                    + endpoint.getEndpoint()
                                    + " -> "
                                    + dependency);
                }
            }

           /* for (ServiceDependencyMetadata dependency :
                    metadata.getServiceDependencies()) {

                ServiceDependencyEntity serviceEntity =
                        new ServiceDependencyEntity();

                serviceEntity.setRepoName(
                        repoName);

                serviceEntity.setSourceService(
                        dependency.getSourceService());

                serviceEntity.setTargetService(
                        dependency.getTargetService());

                serviceEntity.setClientType(
                        dependency.getClientType());

                serviceDependencyRepository.save(
                        serviceEntity);

                System.out.println(
                        "SERVICE DEPENDENCY SAVED: "
                                + dependency.getSourceService()
                                + " -> "
                                + dependency.getTargetService());
            }*/

            Set<String> uniqueDependencies =
                    new HashSet<>(
                            metadata.getDependencies());

            for (String dependency :
                    uniqueDependencies) {

                if (dependency == null
                        || dependency.isBlank()) {

                    continue;
                }

                dependency =
                        dependency.trim();

                if (IGNORED_TYPES.contains(
                        dependency)) {

                    continue;
                }

                if (dependency.equals(
                        metadata.getClassName())) {

                    continue;
                }

                ClassDependencyEntity dependencyEntity =
                        new ClassDependencyEntity();

                dependencyEntity.setRepoName(
                        repoName);

                dependencyEntity.setSourceClass(
                        metadata.getClassName());

                dependencyEntity.setTargetClass(
                        dependency);

                dependencyRepository.save(
                        dependencyEntity);


            }

            extractVariableTypes(
                    repoName,
                    metadata.getClassName(),
                    metadata.getSourceCode(),
                    metadata.getFilePath());

            extractFieldReferences(
                    metadata.getSourceCode(),
                    metadata.getClassName(),
                    repoName,
                    metadata.getFilePath());

            extractMethodReferences(
                    metadata.getSourceCode(),
                    metadata.getClassName(),
                    repoName,
                    metadata.getFilePath());

            extractMethodCalls(
                    repoName,
                    metadata.getClassName(),
                    metadata.getSourceCode(),
                    metadata.getFilePath());
            extractFeignClients(
                    repoName,
                    metadata.getClassName(),
                    metadata.getSourceCode());
            extractRestTemplateCalls(
                    repoName,
                    metadata.getClassName(),
                    metadata.getSourceCode());
        }

        System.out.println(
                "INDEXED CLASSES = "
                        + savedClasses);

        return savedClasses;
    }

    private void extractFieldReferences(
            String sourceCode,
            String currentClass,
            String repoName,
            String filePath) {

        Set<String> uniqueReferences =
                new HashSet<>();
        System.out.println(
                "FIELD REFERENCE SCAN: "
                        + currentClass);
        Pattern getterPattern =
                Pattern.compile(
                        "(\\w+)\\.get([A-Z]\\w+)\\(");

        Matcher matcher =
                getterPattern.matcher(
                        sourceCode);


        while (matcher.find()) {

            String variableName =
                    matcher.group(1);

            String fieldName =
                    matcher.group(2);



            System.out.println(
                    variableName
                            + " -> "
                            + fieldName);

           // if (variableName.equalsIgnoreCase(
             //       "owner")) {




                FieldReferenceEntity ref =
                        new FieldReferenceEntity();

                ref.setRepoName(repoName);

                ref.setSourceClass(
                        currentClass);

            ref.setFilePath(
                    filePath);

            ref.setLineNumber(
                    getLineNumber(
                            sourceCode,
                            matcher.start()));

            ref.setCodeSnippet(
                    matcher.group());

            List<VariableTypeEntity> variables =
                    variableTypeRepository
                            .findBySourceClassIgnoreCaseAndVariableNameIgnoreCase(
                                    currentClass,
                                    variableName);

            if (variables.isEmpty()) {
                continue;
            }

            VariableTypeEntity variableType =
                    variables.get(0);

            String targetClass =
                    variableType.getTypeName();

            if (targetClass == null) {
                continue;
            }

            String key =
                    currentClass
                            + "|"
                            + targetClass
                            + "|"
                            + fieldName.toLowerCase();

                ref.setTargetClass(targetClass);

                ref.setFieldName(
                        fieldName.toLowerCase());

                ref.setReferenceType(
                        "GETTER");
            System.out.println(
                    "SAVING FIELD REF = "
                            + currentClass
                            + " -> "
                            + targetClass
                            + "."
                            + fieldName);

                if (!uniqueReferences.add(key)) {
                    continue;
                }
                fieldReferenceRepository
                        .save(ref);
            //}
        }
    }

    private void extractMethodReferences(
            String sourceCode,
            String currentClass,
            String repoName,
            String filePath) {

        if (sourceCode == null
                || sourceCode.isBlank()) {

            return;
        }

        System.out.println(
                "METHOD REFERENCE SCAN = "
                        + currentClass);

        Pattern pattern =
                Pattern.compile(
                        "(\\w+)\\.(\\w+)\\(");

        Matcher matcher =
                pattern.matcher(
                        sourceCode);

        Set<String> uniqueMethods =
                new HashSet<>();

        while (matcher.find()) {

            String variable =
                    matcher.group(1);

            String method =
                    matcher.group(2);

            System.out.println(
                    "METHOD REF = "
                            + variable
                            + "."
                            + method);

            List<VariableTypeEntity> variables =
                    variableTypeRepository
                            .findBySourceClassIgnoreCaseAndVariableNameIgnoreCase(
                                    currentClass,
                                    variable);

            if (variables.isEmpty()) {
                continue;
            }

            VariableTypeEntity variableType =
                    variables.get(0);

            String targetClass =
                    variableType.getTypeName();

            if (targetClass == null) {
                continue;
            }


            String key =
                    currentClass
                            + "|"
                            + targetClass
                            + "|"
                            + method;

            if (!uniqueMethods.add(
                    key)) {

                continue;
            }

            MethodReferenceEntity ref =
                    new MethodReferenceEntity();

            ref.setRepoName(
                    repoName);

            ref.setSourceClass(
                    currentClass);

            ref.setTargetClass(targetClass);

            ref.setMethodName(
                    method);

            ref.setFilePath(
                    filePath);

            ref.setLineNumber(
                    getLineNumber(
                            sourceCode,
                            matcher.start()));

            ref.setCodeSnippet(
                    matcher.group());

            ref.setReferenceType(
                    "METHOD_CALL");
            System.out.println(
                    "TARGET CLASS = "
                            + targetClass
                            + " METHOD = "
                            + method);
            methodReferenceRepository
                    .save(ref);

            System.out.println(
                    "METHOD SAVED = "
                            + currentClass
                            + " -> "
                            + targetClass
                            + "."
                            + method);
        }



    }


    private void extractVariableTypes(
            String repoName,
            String currentClass,
            String sourceCode,
            String filePath) {

        System.out.println(
                "VARIABLE TYPE SCAN = "
                        + currentClass);

        Pattern pattern =
                Pattern.compile(
                        "(\\w+(?:<.*?>)?)\\s+(\\w+)\\s*=");
        Pattern fieldPattern =
                Pattern.compile(
                        "(?:private|protected|public)\\s+(?:final\\s+)?(\\w+(?:<.*?>)?)\\s+(\\w+)\\s*;");


        Matcher matcher =
                pattern.matcher(sourceCode);


        Matcher fieldMatcher =
                fieldPattern.matcher(
                        sourceCode);

        while (fieldMatcher.find()) {

            String type =
                    fieldMatcher.group(1);

            String variable =
                    fieldMatcher.group(2);

            VariableTypeEntity entity =
                    new VariableTypeEntity();

            entity.setRepoName(repoName);
            entity.setSourceClass(currentClass);
            entity.setVariableName(variable);
            entity.setTypeName(
                    normalizeType(type));
            entity.setFilePath(
                    filePath);
            entity.setLineNumber(
                    getLineNumber(
                            sourceCode,
                            fieldMatcher.start()));

            variableTypeRepository.save(entity);

            System.out.println(
                    "FIELD TYPE SAVED = "
                            + variable
                            + " -> "
                            + type);
        }

        while (matcher.find()) {

            String type =
                    matcher.group(1);

            String variable =
                    matcher.group(2);

            VariableTypeEntity entity =
                    new VariableTypeEntity();

            entity.setRepoName(repoName);
            entity.setSourceClass(currentClass);
            entity.setVariableName(variable);
            entity.setTypeName(normalizeType(type));
            entity.setFilePath(
                    filePath);

            entity.setLineNumber(
                    getLineNumber(
                            sourceCode,
                            matcher.start()));

            variableTypeRepository.save(entity);

            System.out.println(
                    "TYPE SAVED = "
                            + variable
                            + " -> "
                            + type);
        }
    }

    private String normalizeType(
            String type) {

        if (type == null) {
            return null;
        }

        if (type.contains("<")
                && type.contains(">")) {

            return type.substring(
                    type.indexOf("<") + 1,
                    type.indexOf(">"));
        }

        return type;
    }

    private void extractMethodCalls(
            String repoName,
            String currentClass,
            String sourceCode,
            String filePath) {
        System.out.println(
                "SOURCE CODE = \n"
                        + sourceCode);
        System.out.println(
                "SCANNING METHOD CALLS FOR "
                        + currentClass);
        Set<String> uniqueCalls =
                new HashSet<>();
        String normalizedCode =
                sourceCode.replaceAll(
                        "\\s+",
                        " ");
        Pattern pattern =
                Pattern.compile(
                        "(\\w+)\\s*\\.\\s*(\\w+)\\s*\\(");
        System.out.println(
                "PATTERN = "
                        + pattern);
        Matcher matcher =
                pattern.matcher(
                        normalizedCode);
        System.out.println(
                "NORMALIZED = "
                        + normalizedCode);

        while (matcher.find()) {

            String variable =
                    matcher.group(1);

            String method =
                    matcher.group(2);

            int callPosition =
                    matcher.start();

            String sourceMethod =
                    findContainingMethod(
                            sourceCode,
                            variable,
                            method);

            List<VariableTypeEntity> variables =
                    variableTypeRepository
                            .findBySourceClassIgnoreCaseAndVariableNameIgnoreCase(
                                    currentClass,
                                    variable);
            System.out.println(
                    "METHOD CALL FOUND = "
                            + variable
                            + "."
                            + method);
            System.out.println(
                    "LOOKUP VARIABLE = "
                            + currentClass
                            + "."
                            + variable);

            System.out.println(
                    "FOUND COUNT = "
                            + variables.size());
            if (variables.isEmpty()) {
                continue;
            }

            String targetClass =
                    variables.get(0)
                            .getTypeName();

            if (targetClass == null) {
                continue;
            }
            String key =
                    currentClass
                            + "|"
                            + sourceMethod
                            + "|"
                            + targetClass
                            + "|"
                            + method;
            MethodCallEntity call =
                    new MethodCallEntity();

            call.setRepoName(
                    repoName);

            call.setSourceClass(
                    currentClass);

            call.setSourceMethod(
                    sourceMethod);

            call.setTargetClass(
                    targetClass);

            call.setTargetMethod(
                    method);

            call.setFilePath(
                    filePath);

            call.setLineNumber(
                    getLineNumber(
                            sourceCode,
                            matcher.start()));

            call.setCodeSnippet(
                    matcher.group());

            if (!uniqueCalls.add(key)) {
                continue;
            }
            methodCallRepository.save(
                    call);

            System.out.println(
                    "METHOD CALL SAVED = "
                            + currentClass
                            + " -> "
                            + targetClass
                            + "."
                            + method);
        }
    }

    private String findContainingMethod(
            String sourceCode,
            String variable,
            String methodName) {

        try {

            CompilationUnit cu =
                    StaticJavaParser.parse(
                            sourceCode);

            for (MethodDeclaration method :
                    cu.findAll(
                            MethodDeclaration.class)) {

                String normalized =
                        method.toString()
                                .replaceAll(
                                        "\\s+",
                                        " ");

                String target =
                        variable
                                + " ."
                                + methodName;

                if (normalized.contains(target)
                        || normalized.contains(
                        variable + "." + methodName)) {

                    return method
                            .getNameAsString();
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return "UNKNOWN";
    }


    private void extractPomDependencies(
            String repoName,
            Path repoDir) {

        try {

            Path pomFile =
                    repoDir.resolve(
                            "pom.xml");

            if (!Files.exists(
                    pomFile)) {

                return;
            }

            String pom =
                    Files.readString(
                            pomFile);

            Pattern pattern =
                    Pattern.compile(
                            "<artifactId>(.*?)</artifactId>");

            Matcher matcher =
                    pattern.matcher(
                            pom);

            while (matcher.find()) {

                String artifactId =
                        matcher.group(1);

                System.out.println(
                        "DEPENDENCY = "
                                + artifactId);
                RepositoryDependencyEntity entity =
                        new RepositoryDependencyEntity();

                entity.setSourceRepo(
                        repoName);

                entity.setTargetRepo(
                        artifactId);

                entity.setDependencyType(
                        "MAVEN");

                entity.setDependencyName(
                        artifactId);

                repositoryDependencyRepository
                        .save(entity);

            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private void extractFeignClients(
            String repoName,
            String currentClass,
            String sourceCode) {

        Pattern pattern =
                Pattern.compile(
                        "@FeignClient\\([^)]*(?:name|value)\\s*=\\s*\"([^\"]+)\"");

        Matcher matcher =
                pattern.matcher(sourceCode);

        while (matcher.find()) {

            String targetService =
                    matcher.group(1).trim();

            System.out.println(
                    "FEIGN TARGET = "
                            + targetService);

            ServiceDependencyEntity entity =
                    new ServiceDependencyEntity();

            entity.setRepoName(repoName);
            entity.setSourceService(repoName);
            entity.setTargetService(targetService);
            entity.setClientType("FEIGN");

            serviceDependencyRepository.save(entity);
        }
    }

    private void extractRestTemplateCalls(
            String repoName,
            String currentClass,
            String sourceCode) {

        Pattern pattern =
                Pattern.compile(
                        "http://([^/\"\\s]+)");

        Matcher matcher =
                pattern.matcher(
                        sourceCode);

        while (matcher.find()) {

            String targetService =
                    matcher.group(1);

            if (!serviceDependencyRepository
                    .existsBySourceServiceAndTargetServiceAndClientType(
                            repoName,
                            targetService,
                            "REST_TEMPLATE")) {

                ServiceDependencyEntity entity =
                        new ServiceDependencyEntity();

                entity.setRepoName(
                        repoName);

                entity.setSourceService(
                        repoName);

                entity.setTargetService(
                        targetService);

                entity.setClientType(
                        "REST_TEMPLATE");

                serviceDependencyRepository
                        .save(entity);

                System.out.println(
                        "REST SAVED = "
                                + repoName
                                + " -> "
                                + targetService);
            }
        }
    }


    private static class MethodRange {

        String methodName;

        int start;

        int end;

        MethodRange(
                String methodName,
                int start,
                int end) {

            this.methodName = methodName;
            this.start = start;
            this.end = end;
        }
    }

    private List<MethodRange> extractMethodRanges(
            String sourceCode) {

        List<MethodRange> methods =
                new ArrayList<>();

        try {

            CompilationUnit cu =
                    StaticJavaParser.parse(
                            sourceCode);

            cu.findAll(MethodDeclaration.class)
                    .forEach(method -> {

                        if (method.getRange().isEmpty()) {
                            return;
                        }

                        Range range =
                                method.getRange().get();

                        methods.add(
                                new MethodRange(
                                        method.getNameAsString(),
                                        range.begin.line,
                                        range.end.line));
                    });

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return methods;
    }

    private int getLineNumber(
            String sourceCode,
            int position) {

        return sourceCode.substring(
                        0,
                        position)
                .split("\n")
                .length;
    }

}