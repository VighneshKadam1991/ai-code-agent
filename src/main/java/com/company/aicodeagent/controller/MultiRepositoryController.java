package com.company.aicodeagent.controller;

import com.company.aicodeagent.dto.MultiRepositoryRequest;
import com.company.aicodeagent.service.MultiRepositoryIndexerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repositories")
public class MultiRepositoryController {

    private final MultiRepositoryIndexerService service;

    public MultiRepositoryController(
            MultiRepositoryIndexerService service) {

        this.service = service;
    }

    @PostMapping("/index-multiple")
    public String indexMultiple(
            @RequestBody
            MultiRepositoryRequest request)
            throws Exception {

        int count = 0;

        for (String repo :
                request.getRepositories()) {

            service.indexRepository(
                    repo);

            count++;
        }

        return "Indexed repositories: "
                + count;
    }
}