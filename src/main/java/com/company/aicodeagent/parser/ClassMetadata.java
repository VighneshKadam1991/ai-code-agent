package com.company.aicodeagent.parser;

import java.util.ArrayList;
import java.util.List;

public class ClassMetadata {
    private String className;
    private String packageName;
    private String sourceCode;
    private String filePath;
    private List<String> fields = new ArrayList<>();
    private List<String> methods = new ArrayList<>();
    private List<String> imports = new ArrayList<>();
    private List<String> annotations = new ArrayList<>();
    private List<String> dependencies =
            new ArrayList<>();

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(
            List<String> dependencies) {
        this.dependencies = dependencies;
    }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public List<String> getFields() { return fields; }
    public List<String> getMethods() { return methods; }
    public List<String> getImports() { return imports; }
    public List<String> getAnnotations() { return annotations; }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
