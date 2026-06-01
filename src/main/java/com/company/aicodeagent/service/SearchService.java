package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.repository.JavaClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final JavaClassRepository repository;

    public SearchService(
            JavaClassRepository repository) {

        this.repository = repository;
    }

    public List<JavaClassEntity> search(
            String keyword) {

        keyword = keyword
                .replace("\"", "")
                .trim();

        System.out.println(
                "SEARCHING FOR = "
                        + keyword);

        List<JavaClassEntity> results =
                repository.searchByFilePath(
                        keyword);

        System.out.println(
                "MATCHES FOUND = "
                        + results.size());

        return results;
    }
}