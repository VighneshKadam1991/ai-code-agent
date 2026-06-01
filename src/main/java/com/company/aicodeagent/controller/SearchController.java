package com.company.aicodeagent.controller;

import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<JavaClassEntity> search(
            @RequestParam String keyword) {

        return searchService.search(keyword);
    }
}
