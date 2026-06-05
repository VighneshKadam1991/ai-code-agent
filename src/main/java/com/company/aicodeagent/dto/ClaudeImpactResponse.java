package com.company.aicodeagent.dto;

public class ClaudeImpactResponse {

    private String summary;

    private String risk;

    private String migrationPlan;

    private String deploymentOrder;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getMigrationPlan() {
        return migrationPlan;
    }

    public void setMigrationPlan(String migrationPlan) {
        this.migrationPlan = migrationPlan;
    }

    public String getDeploymentOrder() {
        return deploymentOrder;
    }

    public void setDeploymentOrder(String deploymentOrder) {
        this.deploymentOrder = deploymentOrder;
    }
}
