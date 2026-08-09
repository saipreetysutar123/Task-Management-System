package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.ProjectResponseDTO;
import com.saipreety.taskmanagement.dto.ProjectRequestDTO;
import java.util.List;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO request);
    List<ProjectResponseDTO> getAllProjects();
    ProjectResponseDTO getProjectById(Long id);
    ProjectResponseDTO updateProject(ProjectRequestDTO request, Long id);
    void deleteProject(Long id);
}
