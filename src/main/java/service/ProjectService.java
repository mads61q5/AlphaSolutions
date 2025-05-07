package service;
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

    public Project getProjectById(int projectID) {
        return projectRepository.findById(projectID);
    }

    public void createProject(Project project) {
        project.setProjectStatus("NOT_STARTED");
        project.setProjectPriority("LOW_PRIORITY");
        project.setProjectTimeSpent(0);
        projectRepository.save(project);
    }

    public void updateProject(Project project) {
        projectRepository.update(project);
    }

    public void deleteProject(int projectID) {
        projectRepository.delete(projectID);
    }

    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }
}
