package com.company.aicodeagent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "impact_results")
public class ImpactResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String changeType;

    private String entityName;

    private String impactedClass;

    private String impactedMethod;

    @Column(length = 5000)
    private String impactReason;

    public Long getId() {
        return id;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getImpactedClass() {
        return impactedClass;
    }

    public void setImpactedClass(String impactedClass) {
        this.impactedClass = impactedClass;
    }

    public String getImpactedMethod() {
        return impactedMethod;
    }

    public void setImpactedMethod(String impactedMethod) {
        this.impactedMethod = impactedMethod;
    }

    public String getImpactReason() {
        return impactReason;
    }

    public void setImpactReason(String impactReason) {
        this.impactReason = impactReason;
    }
}