package com.company.aicodeagent.dto;

public class GraphEdge {

    private String source;
    private String target;

    public GraphEdge() {
    }

    public GraphEdge(
            String source,
            String target) {

        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(
            String source) {

        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(
            String target) {

        this.target = target;
    }
}