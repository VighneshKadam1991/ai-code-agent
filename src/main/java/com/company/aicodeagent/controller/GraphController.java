package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.GraphResponse;
import com.company.aicodeagent.service.GraphService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private final GraphService
            graphService;

    public GraphController(
            GraphService graphService) {

        this.graphService =
                graphService;
    }

    @GetMapping("/services")
    public GraphResponse
    services() {

        return graphService
                .buildGraph();
    }
}