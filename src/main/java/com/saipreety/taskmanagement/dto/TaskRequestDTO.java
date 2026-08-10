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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title should not be empty")
    private String title;

    @NotBlank(message = "Description should not be empty")
    @Size(min = 10, message = "Description should contain at least 10 characters")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Project ID is required")
    private Long projectId;
}
