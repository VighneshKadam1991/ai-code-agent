package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.*;
import com.company.aicodeagent.parser.ClassMetadata;
import com.company.aicodeagent.repository.*;
import org.springframework.stereotype.Service;
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
                    variableTypeRepository) {

        this.repository = repository;
        this.parserService = parserService;
        this.dependencyRepository = dependencyRepository;
        this.apiEndpointRepository = apiEndpointRepository;
        this.serviceDependencyRepository =
                serviceDependencyRepository;
        this.apiFlowRepository =
                apiFlowRepository;
        this.fieldReferenceRepository =
                fieldReferenceRepository;
        this.methodReferenceRepository =
                methodReferenceRepository;

        this.variableTypeRepository =
                variableTypeRepository;
    }

    public int indexRepository(
            File repoFolder,
            String repoName)
            throws Exception {

        List<ClassMetadata> classes =
                parserService.parseRepository(
                        repoFolder);

        int savedClasses = 0;

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

            for (ServiceDependencyMetadata dependency :
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
            }

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
                    metadata.getSourceCode());

            extractFieldReferences(
                    metadata.getSourceCode(),
                    metadata.getClassName(),
                    repoName);

            extractMethodReferences(
                    metadata.getSourceCode(),
                    metadata.getClassName(),
                    repoName);
        }

        System.out.println(
                "INDEXED CLASSES = "
                        + savedClasses);

        return savedClasses;
    }

    private void extractFieldReferences(
            String sourceCode,
            String currentClass,
            String repoName) {

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
            String repoName) {

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
            String sourceCode) {

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
            entity.setTypeName(type);

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
}