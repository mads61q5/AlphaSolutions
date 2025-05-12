package org.example.alphasolutions.service;
import java.util.List;

import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public Project getProjectById(int projectID) {
        return projectRepository.findById(projectID);
    }
    public Project createProject(Project project) {
        projectRepository.save(project);
        return project;
    }

    public void updateProject(Project project) {
        projectRepository.update(project);
    }

    public void deleteProject(int projectID) {
        projectRepository.delete(projectID);
    }

    }

