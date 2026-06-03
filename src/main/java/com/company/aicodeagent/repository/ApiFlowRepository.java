package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.ApiFlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiFlowRepository
        extends JpaRepository<ApiFlowEntity, Long> {

    List<ApiFlowEntity>
    findByEndpointContainingIgnoreCase(
            String keyword);
}
