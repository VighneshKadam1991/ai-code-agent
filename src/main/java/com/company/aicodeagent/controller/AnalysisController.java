package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.AnalyzeRequest;
import com.company.aicodeagent.service.ImpactAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final ImpactAnalysisService impactAnalysisService;

    public AnalysisController(
            ImpactAnalysisService impactAnalysisService) {

        this.impactAnalysisService = impactAnalysisService;
    }

    @PostMapping
    public String analyze(
            @RequestBody AnalyzeRequest request) {

        return impactAnalysisService.analyze(
                request.getIssue());
    }
}
