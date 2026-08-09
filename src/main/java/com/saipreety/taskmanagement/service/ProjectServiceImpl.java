package com.saipreety.taskmanagement.service;

import com.saipreety.taskmanagement.dto.ProjectRequestDTO;
import com.saipreety.taskmanagement.dto.ProjectResponseDTO;
import com.saipreety.taskmanagement.entity.ProjectEntity;
import com.saipreety.taskmanagement.exception.ProjectNotFoundException;
import com.saipreety.taskmanagement.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;

    public ProjectServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    private ProjectResponseDTO mapToResponse(ProjectEntity project){
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreatedAt(project.getCreatedAt());
        return response;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request){
        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedAt(LocalDateTime.now());
        ProjectEntity savedProject = repository.save(project);
        return mapToResponse(savedProject);
    }

    public List<ProjectResponseDTO> getAllProjects(){
        List<ProjectEntity> projects = repository.findAll();
        List<ProjectResponseDTO> responseList = new ArrayList<>();
        for (ProjectEntity project : projects) {
            responseList.add(mapToResponse(project));
        }
        return responseList;
    }

    public ProjectResponseDTO getProjectById(Long id){
        Optional<ProjectEntity> optionalProject = repository.findById(id);
        if(optionalProject.isPresent()){
            ProjectEntity project = optionalProject.get();
            return mapToResponse(project);
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
    }

    public ProjectResponseDTO updateProject(ProjectRequestDTO request, Long id){
        Optional<ProjectEntity> projectId = repository.findById(id);
        if(projectId.isPresent()){
            ProjectEntity project = projectId.get();
            project.setName(request.getName());
            project.setDescription(request.getDescription());
            ProjectEntity savedProject = repository.save(project);

            return mapToResponse(savedProject);
        }
        else {
            throw new ProjectNotFoundException("Project not found with id: " + id);
        }
    }

    public void deleteProject(Long id){
        Optional<ProjectEntity> projectId = repository.findById(id);
        if(projectId.isPresent()){
            repository.deleteById(id);
        }
        else {
            throw new ProjectNotFoundException(
                    "Project not found with id: " + id
            );
        }
    }
}
