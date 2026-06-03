package com.company.aicodeagent.repository;

import com.company.aicodeagent.entity.JavaClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JavaClassRepository
        extends JpaRepository<JavaClassEntity, Long> {

    @Query("""
            select j
            from JavaClassEntity j
            where lower(j.filePath)
                  like lower(concat('%', :keyword, '%'))
            """)
    List<JavaClassEntity> searchByFilePath(
            @Param("keyword") String keyword);

    List<JavaClassEntity>
    findByClassNameContainingIgnoreCase(
            String className);

    List<JavaClassEntity>
    findByClassNameIn(
            List<String> classNames);

    List<JavaClassEntity>
    findBySourceCodeContainingIgnoreCase(
            String keyword);


    List<JavaClassEntity>
    findByClassNameIgnoreCase(
            String className);



}