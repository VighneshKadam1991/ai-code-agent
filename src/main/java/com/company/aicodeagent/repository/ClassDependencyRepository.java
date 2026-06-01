package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.ClassDependencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassDependencyRepository
        extends JpaRepository<ClassDependencyEntity, Long> {

    List<ClassDependencyEntity>
    findBySourceClassIgnoreCase(String sourceClass);

    List<ClassDependencyEntity>
    findByTargetClassIgnoreCase(String targetClass);


}
