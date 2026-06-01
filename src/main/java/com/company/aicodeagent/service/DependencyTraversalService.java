package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.ClassDependencyEntity;
import com.company.aicodeagent.repository.ClassDependencyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DependencyTraversalService {
    private final ClassDependencyRepository repository;

    public DependencyTraversalService(ClassDependencyRepository repository) {
        this.repository = repository;
    }

    public String analyze(String className) {
        List<ClassDependencyEntity> impacts =
                repository.findByTargetClassIgnoreCase(className);

        StringBuilder sb = new StringBuilder();
        sb.append("Root Impact: ").append(className).append("\n\n");

        for (ClassDependencyEntity d : impacts) {
            sb.append("Affected Class: ")
              .append(d.getSourceClass())
              .append("\nDepends On: ")
              .append(d.getTargetClass())
              .append("\n\n");
        }
        return sb.toString();
    }
}
