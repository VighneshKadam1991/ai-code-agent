package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.VariableTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariableTypeRepository
        extends JpaRepository<VariableTypeEntity, Long> {

    List<VariableTypeEntity>
    findBySourceClassIgnoreCase(
            String sourceClass);

    Optional<VariableTypeEntity>
    findBySourceClassIgnoreCaseAndVariableNameIgnoreCase(
            String sourceClass,
            String variableName);
}