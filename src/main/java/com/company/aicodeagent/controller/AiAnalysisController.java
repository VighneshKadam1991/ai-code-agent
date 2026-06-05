package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.*;
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

    @PostMapping("/change-analysis")
    public AiAnalysisResponse analyze(
            @RequestBody
            ChangeAnalysisRequest request) {

        return service
                .analyze(request);
    }

    @PostMapping("/chat")
    public AiAnalysisResponse chat(
            @RequestBody
            AiChatRequest request) {

        return service.chat(
                request.getPrompt());
    }
    @PostMapping("/generate-patch")
    public List<CodePatch> generatePatch(
            @RequestBody
            PatchRequest request) {

        return service.generatePatch(
                request.getPrompt());
    }

    @PostMapping("/generate-full-patch")
    public List<GeneratedPatch>
    generateFullPatch(
            @RequestBody
            PatchRequest request) {

        return service
                .generateFullPatch(
                        request.getPrompt());
    }
}