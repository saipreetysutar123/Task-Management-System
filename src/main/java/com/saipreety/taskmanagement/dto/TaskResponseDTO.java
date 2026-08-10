package com.saipreety.taskmanagement.dto;

import com.saipreety.taskmanagement.entity.TaskPriority;
import com.saipreety.taskmanagement.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private Long userId;

    private Long projectId;

    private LocalDateTime createdAt;
}
