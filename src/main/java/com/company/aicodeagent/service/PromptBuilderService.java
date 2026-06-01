package com.company.aicodeagent.service;

import com.company.aicodeagent.entity.JavaClassEntity;
import com.company.aicodeagent.repository.JavaClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptBuilderService {

    private final JavaClassRepository repository;

    public PromptBuilderService(JavaClassRepository repository) {
        this.repository = repository;
    }

    public String buildPrompt(String keyword) {

        List<JavaClassEntity> classes =
                repository.findAll();

        StringBuilder sb = new StringBuilder();

        sb.append("TASK:\n");
        sb.append("Analyze impact for: ")
          .append(keyword)
          .append("\n\n");

        for (JavaClassEntity c : classes) {

            if (c.getSourceCode() != null &&
                c.getSourceCode().contains(keyword)) {

                sb.append("FILE: ")
                  .append(c.getFilePath())
                  .append("\n");

                sb.append("CLASS: ")
                  .append(c.getClassName())
                  .append("\n\n");

                sb.append(c.getSourceCode())
                  .append("\n\n====================\n\n");
            }
        }

        return sb.toString();
    }
}
