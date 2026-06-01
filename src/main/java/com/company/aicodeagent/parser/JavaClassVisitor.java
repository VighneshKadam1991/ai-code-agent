package com.company.aicodeagent.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaClassVisitor
        extends VoidVisitorAdapter<ClassMetadata> {

    @Override
    public void visit(
            FieldDeclaration fd,
            ClassMetadata metadata) {

        fd.getVariables()
                .forEach(v ->
                        metadata.getFields()
                                .add(v.getNameAsString()));

        super.visit(fd, metadata);
    }

    @Override
    public void visit(
            ConstructorDeclaration cd,
            ClassMetadata metadata) {

        cd.getParameters()
                .forEach(parameter ->
                        metadata.getDependencies()
                                .add(parameter.getType()
                                        .asString()));

        super.visit(cd, metadata);
    }

    @Override
    public void visit(
            MethodDeclaration md,
            ClassMetadata metadata) {

        metadata.getMethods()
                .add(md.getNameAsString());

        super.visit(md, metadata);
    }

    @Override
    public void visit(
            ClassOrInterfaceDeclaration cid,
            ClassMetadata metadata) {

        cid.getExtendedTypes()
                .forEach(type -> {

                    metadata.getDependencies()
                            .add(type.getNameAsString());

                    type.getTypeArguments()
                            .ifPresent(arguments ->
                                    arguments.forEach(arg ->
                                            metadata.getDependencies()
                                                    .add(arg.asString())));
                });

        cid.getImplementedTypes()
                .forEach(type -> {

                    metadata.getDependencies()
                            .add(type.getNameAsString());

                    type.getTypeArguments()
                            .ifPresent(arguments ->
                                    arguments.forEach(arg ->
                                            metadata.getDependencies()
                                                    .add(arg.asString())));
                });

        super.visit(cid, metadata);
    }
}