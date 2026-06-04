package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.ImpactResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpactResultRepository
        extends JpaRepository<
        ImpactResultEntity,
        Long> {
}