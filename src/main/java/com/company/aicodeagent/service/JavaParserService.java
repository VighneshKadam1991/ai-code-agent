package com.company.aicodeagent.service;

import com.company.aicodeagent.parser.ClassMetadata;
import com.company.aicodeagent.parser.JavaClassVisitor;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
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
                .filter(path ->
                        !path.getFileName()
                                .toString()
                                .equals("package-info.java"))
                .filter(path ->
                        !path.getFileName()
                                .toString()
                                .equals("module-info.java"))
                .forEach(path -> {

                    try {

                        String source =
                                Files.readString(path);

                        CompilationUnit cu =
                                StaticJavaParser.parse(source);

                        ClassMetadata metadata =
                                new ClassMetadata();

                        metadata.setSourceCode(
                                source);

                        metadata.setFilePath(
                                path.toString());

                        cu.getPackageDeclaration()
                                .ifPresent(pkg ->
                                        metadata.setPackageName(
                                                pkg.getNameAsString()));

                        String className = null;

                        /*
                         * Class / Interface
                         */
                        var classDecl =
                                cu.findFirst(
                                        ClassOrInterfaceDeclaration.class);

                        if (classDecl.isPresent()) {

                            className =
                                    classDecl.get()
                                            .getNameAsString();
                        }

                        /*
                         * Enum
                         */
                        if (className == null) {

                            var enumDecl =
                                    cu.findFirst(
                                            EnumDeclaration.class);

                            if (enumDecl.isPresent()) {

                                className =
                                        enumDecl.get()
                                                .getNameAsString();
                            }
                        }

                        /*
                         * Record
                         */
                        if (className == null) {

                            var recordDecl =
                                    cu.findFirst(
                                            RecordDeclaration.class);

                            if (recordDecl.isPresent()) {

                                className =
                                        recordDecl.get()
                                                .getNameAsString();
                            }
                        }

                        /*
                         * Annotation
                         */
                        if (className == null) {

                            var annotationDecl =
                                    cu.findFirst(
                                            AnnotationDeclaration.class);

                            if (annotationDecl.isPresent()) {

                                className =
                                        annotationDecl.get()
                                                .getNameAsString();
                            }
                        }

                        metadata.setClassName(
                                className);

                        cu.getImports()
                                .forEach(importDecl ->
                                        metadata.getImports()
                                                .add(importDecl.getNameAsString()));

                        cu.findAll(
                                        ClassOrInterfaceDeclaration.class)
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