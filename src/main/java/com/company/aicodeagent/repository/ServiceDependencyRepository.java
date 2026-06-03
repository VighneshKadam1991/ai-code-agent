package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.ServiceDependencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceDependencyRepository
        extends JpaRepository<ServiceDependencyEntity, Long> {
}