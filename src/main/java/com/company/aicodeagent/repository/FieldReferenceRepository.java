package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.FieldReferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldReferenceRepository
        extends JpaRepository<FieldReferenceEntity, Long> {

    List<FieldReferenceEntity>
    findByTargetClassIgnoreCaseAndFieldNameIgnoreCase(
            String targetClass,
            String fieldName);

    List<FieldReferenceEntity>
    findByTargetClassIgnoreCase(
            String targetClass);

    List<FieldReferenceEntity>
    findByTargetClassAndFieldName(
            String targetClass,
            String fieldName);

    List<FieldReferenceEntity>
    findByTargetClass(
            String targetClass);

    List<FieldReferenceEntity>
    findByFieldName(
            String fieldName);

    List<FieldReferenceEntity>
    findByFieldNameIgnoreCase(
            String fieldName);
}
