package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.ClassDependencyEntity;
import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.parser.ClassMetadata;
import com.company.aicodeagent.repository.ClassDependencyRepository;
import com.company.aicodeagent.repository.JavaClassRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class JavaIndexerService {

    private final JavaClassRepository repository;

    private final JavaParserService parserService;

    private final ClassDependencyRepository dependencyRepository;

    public JavaIndexerService(
            JavaClassRepository repository,
            JavaParserService parserService,
            ClassDependencyRepository dependencyRepository) {

        this.repository = repository;
        this.parserService = parserService;
        this.dependencyRepository = dependencyRepository;
    }

    public int indexRepository(
            File repoFolder,
            String repoName)
            throws Exception {

        List<ClassMetadata> classes =
                parserService.parseRepository(
                        repoFolder);

        for (ClassMetadata metadata : classes) {

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
            System.out.println(
                    "CLASS = "
                            + metadata.getClassName());

            System.out.println(
                    "DEPENDENCIES = "
                            + metadata.getDependencies());
            repository.save(entity);

            // Save dependencies
            for (String dependency :
                    metadata.getDependencies()) {

                if (dependency == null ||
                        dependency.isBlank()) {
                    continue;
                }

                if (dependency.equals("String") ||
                        dependency.equals("Integer") ||
                        dependency.equals("Long") ||
                        dependency.equals("Boolean") ||
                        dependency.equals("Double") ||
                        dependency.equals("LocalDate")) {
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

                System.out.println(
                        "DEPENDENCY SAVED: "
                                + metadata.getClassName()
                                + " -> "
                                + dependency);
            }
        }

        return classes.size();
    }
}