package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.AiAnalysisRequest;
import com.company.aicodeagent.dto.AiAnalysisResponse;
import com.company.aicodeagent.service.AiAnalysisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis/ai")
public class AiAnalysisController {

    private final AiAnalysisService service;

    public AiAnalysisController(
            AiAnalysisService service) {

        this.service = service;
    }

    @PostMapping
    public AiAnalysisResponse analyze(
            @RequestBody AiAnalysisRequest request) {

        String result =
                service.analyze(
                        request.getIssue());

        return new AiAnalysisResponse(
                result);
    }
}