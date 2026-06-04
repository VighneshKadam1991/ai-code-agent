package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.ServiceDependencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceDependencyRepository
        extends JpaRepository<ServiceDependencyEntity, Long> {

    List<ServiceDependencyEntity>
    findByTargetService(
            String targetService);

    boolean existsBySourceServiceAndTargetServiceAndClientType(
            String sourceService,
            String targetService,
            String clientType);
}