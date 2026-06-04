package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.MethodReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MethodReferenceRepository
        extends JpaRepository<MethodReferenceEntity, Long> {

    List<MethodReferenceEntity>
    findByTargetClassIgnoreCase(
            String targetClass);

    List<MethodReferenceEntity>
    findByTargetClassIgnoreCaseAndMethodNameIgnoreCase(
            String targetClass,
            String methodName);

    List<MethodReferenceEntity>
    findByTargetClassAndMethodName(
            String targetClass,
            String methodName);

    List<MethodReferenceEntity>
    findByTargetClass(
            String targetClass);
}