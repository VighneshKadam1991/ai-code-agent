package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.SearchResultDto;
import com.company.aicodeagent.service.RepositorySearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class RepositorySearchController {

    private final RepositorySearchService service;

    public RepositorySearchController(
            RepositorySearchService service) {

        this.service = service;
    }

    @GetMapping("/class")
    public List<SearchResultDto>
    searchClass(
            @RequestParam String name) {

        return service.searchClass(name);
    }

    @GetMapping("/code")
    public List<SearchResultDto>
    searchCode(
            @RequestParam String keyword) {

        return service.searchCode(keyword);
    }
}