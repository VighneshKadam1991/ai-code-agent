package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.AiAnalysisRequest;
import com.company.aicodeagent.dto.AiAnalysisResponse;
import com.company.aicodeagent.dto.ImpactFileResponse;
import com.company.aicodeagent.service.AiAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analysis/ai")
public class AiAnalysisController {

    private final AiAnalysisService service;
    private final SearchController searchController;

    public AiAnalysisController(
            AiAnalysisService service,
            SearchController searchController) {

        this.service = service;
        this.searchController = searchController;
    }

    @PostMapping
    public AiAnalysisResponse analyze(
            @RequestBody AiAnalysisRequest request) {

        List<ImpactFileResponse> impactedFiles =
                searchController.filesImpact(
                        "Owner");

        System.out.println(
                "IMPACT FILES = "
                        + impactedFiles.size());

        for (ImpactFileResponse file :
                impactedFiles) {

            System.out.println(
                    "FILE SENT = "
                            + file.getClassName());
        }

        String result =
                service.analyze(
                        request.getIssue());

        return new AiAnalysisResponse(
                result);
    }
}