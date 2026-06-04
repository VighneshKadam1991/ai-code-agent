package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.RepositoryDependencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryDependencyRepository
        extends JpaRepository<
                RepositoryDependencyEntity,
                Long> {

    List<RepositoryDependencyEntity>
    findBySourceRepo(
            String sourceRepo);
}
