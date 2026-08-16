package com.saipreety.taskmanagement.repository;

import com.saipreety.taskmanagement.entity.TaskEntity;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Page<TaskEntity> findByStatus(
            TaskStatus status,
            Pageable pageable
    );
    Page<TaskEntity> findByPriority(
            TaskPriority priority,
            Pageable pageable
    );
    Page<TaskEntity> findByProjectId(
            Long projectId,
            Pageable pageable
    );
    Page<TaskEntity> findByUserId(
            Long id,
            Pageable pageable
    );
    Page<TaskEntity> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );
}
