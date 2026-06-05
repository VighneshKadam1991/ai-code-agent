package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.MethodCallEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MethodCallRepository
        extends JpaRepository<
                MethodCallEntity,
                Long> {

    List<MethodCallEntity>
    findByTargetClassIgnoreCaseAndTargetMethodIgnoreCase(
            String targetClass,
            String targetMethod);

    List<MethodCallEntity>
    findByTargetClassAndTargetMethod(
            String targetClass,
            String targetMethod);

    List<MethodCallEntity>
    findByTargetClass(
            String targetClass);

    List<MethodCallEntity>
    findByRepoName(
            String repoName);

    List<MethodCallEntity>
    findByTargetMethodIgnoreCase(
            String targetMethod);
}