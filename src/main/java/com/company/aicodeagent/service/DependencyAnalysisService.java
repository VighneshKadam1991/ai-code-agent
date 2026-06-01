package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.ClassDependencyEntity;
import com.company.aicodeagent.repository.ClassDependencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependencyAnalysisService {

    private final ClassDependencyRepository repository;

    public DependencyAnalysisService(
            ClassDependencyRepository repository) {
        this.repository = repository;
    }

    public List<ClassDependencyEntity>
    findImpacts(String className) {

        return repository
                .findByTargetClassIgnoreCase(className);
    }
}
