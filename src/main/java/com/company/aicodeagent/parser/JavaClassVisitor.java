package com.company.aicodeagent.parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaClassVisitor
        extends VoidVisitorAdapter<ClassMetadata> {

    private String currentController;

    private boolean restController = false;

    @Override
    public void visit(
            FieldDeclaration fd,
            ClassMetadata metadata) {

        fd.getVariables()
                .forEach(v ->
                        metadata.getFields()
                                .add(v.getNameAsString()));

        String fieldType =
                normalizeType(
                        fd.getElementType()
                                .asString());

        metadata.getDependencies()
                .add(fieldType);

        fd.getElementType()
                .ifClassOrInterfaceType(type ->
                        type.getTypeArguments()
                                .ifPresent(arguments ->
                                        arguments.forEach(arg ->
                                                metadata.getDependencies()
                                                        .add(
                                                                normalizeType(
                                                                        arg.asString())))));

        super.visit(fd, metadata);
    }

    @Override
    public void visit(
            ConstructorDeclaration cd,
            ClassMetadata metadata) {

        cd.getParameters()
                .forEach(parameter ->
                        metadata.getDependencies()
                                .add(
                                        normalizeType(
                                                parameter.getType()
                                                        .asString())));

        super.visit(cd, metadata);
    }

    @Override
    public void visit(
            ClassOrInterfaceDeclaration cid,
            ClassMetadata metadata) {

        currentController =
                cid.getNameAsString();

        restController =
                cid.getAnnotations()
                        .stream()
                        .anyMatch(a ->
                                a.getNameAsString()
                                        .equals("RestController")
                                        ||
                                        a.getNameAsString()
                                                .equals("Controller"));

        /*
         * FEIGN CLIENT DISCOVERY
         */
        cid.getAnnotations()
                .forEach(annotation -> {

                    if (!annotation
                            .getNameAsString()
                            .equals("FeignClient")) {

                        return;
                    }

                    String targetService =
                            "UNKNOWN";

                    if (annotation instanceof NormalAnnotationExpr normal) {

                        targetService =
                                normal.getPairs()
                                        .stream()
                                        .filter(p ->
                                                p.getNameAsString()
                                                        .equals("name")
                                                        ||
                                                        p.getNameAsString()
                                                                .equals("value"))
                                        .findFirst()
                                        .map(p ->
                                                p.getValue()
                                                        .toString()
                                                        .replace("\"", ""))
                                        .orElse("UNKNOWN");
                    }

                    ServiceDependencyMetadata dependency =
                            new ServiceDependencyMetadata();

                    dependency.setSourceService(
                            metadata.getPackageName());

                    dependency.setTargetService(
                            targetService);

                    dependency.setClientType(
                            "FEIGN");

                    metadata.getServiceDependencies()
                            .add(dependency);

                    System.out.println(
                            "FEIGN FOUND: "
                                    + currentController
                                    + " -> "
                                    + targetService);
                });

        System.out.println(
                "CONTROLLER CHECK: "
                        + currentController
                        + " -> "
                        + restController);
        cid.getExtendedTypes()
                .forEach(type -> {

                    metadata.getDependencies()
                            .add(
                                    normalizeType(
                                            type.getNameAsString()));

                    type.getTypeArguments()
                            .ifPresent(arguments ->
                                    arguments.forEach(arg ->
                                            metadata.getDependencies()
                                                    .add(
                                                            normalizeType(
                                                                    arg.asString()))));
                });

        cid.getImplementedTypes()
                .forEach(type -> {

                    metadata.getDependencies()
                            .add(
                                    normalizeType(
                                            type.getNameAsString()));

                    type.getTypeArguments()
                            .ifPresent(arguments ->
                                    arguments.forEach(arg ->
                                            metadata.getDependencies()
                                                    .add(
                                                            normalizeType(
                                                                    arg.asString()))));
                });

        super.visit(cid, metadata);
    }

    @Override
    public void visit(
            MethodDeclaration md,
            ClassMetadata metadata) {

        metadata.getMethods()
                .add(md.getNameAsString());

        for (AnnotationExpr annotation :
                md.getAnnotations()) {

            String annotationName =
                    annotation.getNameAsString();

            String httpMethod = null;

            if ("GetMapping".equals(annotationName)) {
                httpMethod = "GET";
            }
            else if ("PostMapping".equals(annotationName)) {
                httpMethod = "POST";
            }
            else if ("PutMapping".equals(annotationName)) {
                httpMethod = "PUT";
            }
            else if ("DeleteMapping".equals(annotationName)) {
                httpMethod = "DELETE";
            }
            else if ("RequestMapping".equals(annotationName)) {
                httpMethod = "REQUEST";
            }

            if (httpMethod == null) {
                continue;
            }

            if (!restController) {
                continue;
            }

            String endpoint =
                    md.getNameAsString();

            if (annotation instanceof SingleMemberAnnotationExpr single) {

                endpoint =
                        single.getMemberValue()
                                .toString()
                                .replace("\"", "");
            }

            if (annotation instanceof NormalAnnotationExpr normal) {

                endpoint =
                        normal.getPairs()
                                .stream()
                                .findFirst()
                                .map(p ->
                                        p.getValue()
                                                .toString()
                                                .replace("\"", ""))
                                .orElse(endpoint);
            }

            ApiEndpointMetadata apiEndpoint =
                    new ApiEndpointMetadata();

            apiEndpoint.setControllerClass(
                    currentController);

            apiEndpoint.setHttpMethod(
                    httpMethod);

            apiEndpoint.setEndpoint(
                    endpoint);
            System.out.println(
                    "API FOUND: "
                            + currentController
                            + " "
                            + httpMethod
                            + " "
                            + endpoint);
            metadata.getEndpoints()
                    .add(apiEndpoint);
        }

        super.visit(md, metadata);
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
                    type.lastIndexOf(">"));
        }

        return type;
    }

}