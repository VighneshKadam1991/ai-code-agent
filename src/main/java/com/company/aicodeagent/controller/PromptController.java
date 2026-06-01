package com.company.aicodeagent.controller;

import com.company.aicodeagent.service.PromptBuilderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prompt")
public class PromptController {

    private final PromptBuilderService service;

    public PromptController(PromptBuilderService service) {
        this.service = service;
    }

    @GetMapping
    public String prompt(@RequestParam String keyword) {
        return service.buildPrompt(keyword);
    }
}
