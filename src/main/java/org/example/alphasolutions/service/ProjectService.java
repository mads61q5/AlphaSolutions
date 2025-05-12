package org.example.alphasolutions.service;
import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public Project getProjectByID(int projectID) {
        return projectRepository.findByID(projectID);
    }
    public void createProject(Project project) {
        projectRepository.save(project);
    }

    public void updateProject(Project project) {
        projectRepository.update(project);
    }

    public void deleteProject(int projectID) {
        projectRepository.delete(projectID);
    }

    }

