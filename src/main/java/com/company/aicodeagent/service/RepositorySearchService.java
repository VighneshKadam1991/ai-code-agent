package com.company.aicodeagent.service;

import com.company.aicodeagent.dto.SearchResultDto;
import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.repository.JavaClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositorySearchService {

    private final JavaClassRepository repository;

    public RepositorySearchService(
            JavaClassRepository repository) {

        this.repository = repository;
    }

    public List<SearchResultDto>
    searchClass(String className) {

        return repository
                .findByClassNameContainingIgnoreCase(
                        className)
                .stream()
                .map(c ->
                        new SearchResultDto(
                                c.getRepoName(),
                                c.getClassName(),
                                c.getFilePath()))
                .toList();
    }

    public List<SearchResultDto>
    searchCode(String keyword) {

        return repository
                .findBySourceCodeContainingIgnoreCase(
                        keyword)
                .stream()
                .map(c ->
                        new SearchResultDto(
                                c.getRepoName(),
                                c.getClassName(),
                                c.getFilePath()))
                .toList();
    }
}