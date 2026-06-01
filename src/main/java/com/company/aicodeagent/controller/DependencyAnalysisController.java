package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.DependencyAnalysisRequest;
import com.company.aicodeagent.service.GraphTraversalService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/analysis/dependencies")
public class DependencyAnalysisController {

    private final GraphTraversalService graphTraversalService;

    public DependencyAnalysisController(
            GraphTraversalService graphTraversalService) {

        this.graphTraversalService =
                graphTraversalService;
    }

    @PostMapping
    public String analyze(
            @RequestBody DependencyAnalysisRequest request) {

        Set<String> impacted =
                graphTraversalService
                        .findImpactedClasses(
                                request.getIssue());

        StringBuilder sb =
                new StringBuilder();

        sb.append("Root Impact: ")
                .append(request.getIssue())
                .append("\n\n");

        for(String clazz : impacted) {

            if(!clazz.equalsIgnoreCase(
                    request.getIssue())) {

                sb.append("Affected Class: ")
                        .append(clazz)
                        .append("\n");
            }
        }

        return sb.toString();
    }
}