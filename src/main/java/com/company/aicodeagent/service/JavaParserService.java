package com.company.aicodeagent.service;

import com.company.aicodeagent.parser.ClassMetadata;
import com.company.aicodeagent.parser.JavaClassVisitor;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class JavaParserService {

    public List<ClassMetadata> parseRepository(
            File repoDir) throws Exception {

        ParserConfiguration config =
                new ParserConfiguration();

        config.setLanguageLevel(
                ParserConfiguration.LanguageLevel.BLEEDING_EDGE);

        StaticJavaParser.setConfiguration(config);

        List<ClassMetadata> result =
                new ArrayList<>();

        Files.walk(repoDir.toPath())
                .filter(path ->
                        path.toString().endsWith(".java"))
                .forEach(path -> {

                    try {

                        String source =
                                Files.readString(path);

                        CompilationUnit cu =
                                StaticJavaParser.parse(source);

                        ClassMetadata metadata =
                                new ClassMetadata();

                        metadata.setSourceCode(source);

                        metadata.setFilePath(
                                path.toString());

                        cu.getPackageDeclaration()
                                .ifPresent(pkg ->
                                        metadata.setPackageName(
                                                pkg.getNameAsString()));

                        cu.findFirst(
                                        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                                .ifPresent(clazz ->
                                        metadata.setClassName(
                                                clazz.getNameAsString()));

                        cu.getImports()
                                .forEach(importDecl ->
                                        metadata.getImports()
                                                .add(importDecl.getNameAsString()));

                        cu.findAll(
                                        com.github.javaparser.ast.body.ClassOrInterfaceDeclaration.class)
                                .forEach(clazz ->
                                        clazz.getAnnotations()
                                                .forEach(annotation ->
                                                        metadata.getAnnotations()
                                                                .add(annotation.getNameAsString())));

                        new JavaClassVisitor()
                                .visit(cu, metadata);

                        result.add(metadata);

                    } catch (Exception ex) {

                        System.out.println(
                                "Failed parsing: "
                                        + path);

                        System.out.println(
                                ex.getMessage());
                    }

                });

        return result;
    }
}