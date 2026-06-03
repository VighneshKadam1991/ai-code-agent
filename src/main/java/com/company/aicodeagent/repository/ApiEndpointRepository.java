package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.ApiEndpointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiEndpointRepository
        extends JpaRepository<ApiEndpointEntity, Long> {

    List<ApiEndpointEntity>
    findByEndpointContainingIgnoreCase(
            String keyword);
}