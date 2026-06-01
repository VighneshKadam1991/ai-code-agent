package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.ClassDependencyEntity;
import com.company.aicodeagent.repository.ClassDependencyRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphTraversalService {

    private final ClassDependencyRepository repository;

    public GraphTraversalService(
            ClassDependencyRepository repository) {

        this.repository = repository;
    }

    public Set<String> findImpactedClasses(
            String rootClass) {

        Set<String> impacted =
                new HashSet<>();

        Queue<String> queue =
                new LinkedList<>();

        queue.add(rootClass);

        System.out.println(
                "STARTING GRAPH TRAVERSAL");

        while (!queue.isEmpty()) {

            String current =
                    queue.poll();

            System.out.println(
                    "PROCESSING = " + current);

            List<ClassDependencyEntity> deps =
                    repository
                            .findByTargetClassIgnoreCase(
                                    current);

            System.out.println(
                    "FOUND DEPENDENCIES = "
                            + deps.size());

            for (ClassDependencyEntity dep : deps) {

                String source =
                        dep.getSourceClass();

                System.out.println(
                        "DISCOVERED = "
                                + source);

                if (impacted.add(source)) {

                    queue.add(source);

                    System.out.println(
                            "ADDED TO QUEUE = "
                                    + source);
                }
            }
        }

        impacted.add(rootClass);

        System.out.println(
                "FINAL IMPACTED = "
                        + impacted);

        return impacted;
    }
}