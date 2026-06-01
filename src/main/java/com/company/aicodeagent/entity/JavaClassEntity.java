package com.company.aicodeagent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "java_classes")
public class JavaClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoName;
    private String filePath;
    private String packageName;
    private String className;

    @Column(columnDefinition = "TEXT")
    private String sourceCode;

    @Column(columnDefinition = "TEXT")
    private String fields;

    @Column(columnDefinition = "TEXT")
    private String methods;

    @Column(columnDefinition = "TEXT")
    private String imports;

    @Column(columnDefinition = "TEXT")
    private String annotations;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public String getFields() { return fields; }
    public void setFields(String fields) { this.fields = fields; }

    public String getMethods() { return methods; }
    public void setMethods(String methods) { this.methods = methods; }

    public String getImports() { return imports; }
    public void setImports(String imports) { this.imports = imports; }

    public String getAnnotations() { return annotations; }
    public void setAnnotations(String annotations) { this.annotations = annotations; }
}
