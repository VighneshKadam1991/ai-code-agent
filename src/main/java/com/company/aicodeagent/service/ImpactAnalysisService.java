package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.JavaClassEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpactAnalysisService {

    private final SearchService searchService;

    public ImpactAnalysisService(SearchService searchService) {
        this.searchService = searchService;
    }

    public String analyze(String issue) {

        List<JavaClassEntity> matches =
                searchService.search(issue);

        StringBuilder result =
                new StringBuilder();

        result.append("Impacted Files\n");
        result.append("====================\n\n");
        System.out.println(
                "Matches found = "
                        + matches.size());
        for (JavaClassEntity file : matches) {

            result.append("Class: ")
                    .append(file.getClassName())
                    .append("\n");

            result.append("Path: ")
                    .append(file.getFilePath())
                    .append("\n\n");
        }

        return result.toString();
    }
}
