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

        System.out.println(
                "STARTING GRAPH TRAVERSAL");

        Set<String> impacted =
                new HashSet<>();

        Queue<String> queue =
                new LinkedList<>();

        queue.add(rootClass);

        while (!queue.isEmpty()) {

            String current =
                    queue.poll();

            if (current == null) {
                continue;
            }

            current = current.trim();

            System.out.println(
                    "PROCESSING = "
                            + current);

            // Incoming dependencies
            List<ClassDependencyEntity> incoming =
                    repository
                            .findByTargetClassIgnoreCase(
                                    current);
            System.out.println(
                    "LOOKING FOR TARGET = ["
                            + current
                            + "]");

            System.out.println(
                    "INCOMING = "
                            + incoming.size());

            for (ClassDependencyEntity dep
                    : incoming) {

                String source =
                        dep.getSourceClass();

                if (isTestClass(source)) {
                    continue;
                }

                if (impacted.add(source)) {

                    System.out.println(
                            "ADDING INCOMING = "
                                    + source);

                    queue.add(source);
                }
            }
            System.out.println(
                    "LOOKING FOR SOURCE = ["
                            + current
                            + "]");
            // Outgoing dependencies
            List<ClassDependencyEntity> outgoing =
                    repository
                            .findBySourceClassIgnoreCase(
                                    current);

            System.out.println(
                    "OUTGOING = "
                            + outgoing.size());

            for (ClassDependencyEntity dep
                    : outgoing) {

                String target =
                        dep.getTargetClass();

                if (impacted.add(target)) {

                    System.out.println(
                            "ADDING OUTGOING = "
                                    + target);

                    queue.add(target);
                }
            }
        }

        impacted.add(rootClass);

        System.out.println(
                "FINAL IMPACTED = "
                        + impacted);

        return impacted;
    }

    private boolean isTestClass(
            String className) {

        return className.endsWith("Tests")
                || className.endsWith("Test")
                || className.contains(
                "IntegrationTest");
    }

}