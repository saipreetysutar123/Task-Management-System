package com.saipreety.taskmanagement.repository;

import com.saipreety.taskmanagement.entity.TaskEntity;
import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Page<TaskEntity> findByStatus(TaskStatus status, Pageable pageable);
    List<TaskEntity> findByPriority(TaskPriority priority);
    List<TaskEntity> findByProjectId(Long projectId);
    List<TaskEntity> findByUserId(Long id);
    List<TaskEntity> findByTitleContainingIgnoreCase(String title);
}
