package org.example.alphasolutions.service;
import java.util.List;

import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TimeCalculationService timeCalculationService;

    public ProjectService(ProjectRepository projectRepository, TimeCalculationService timeCalculationService) {
        this.projectRepository = projectRepository;
        this.timeCalculationService = timeCalculationService;
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
    
    //--------calculate time from subprojects and update----------
    public void calculateAndUpdateProjectTotalsFromSubProjects(Project project, List<SubProject> subProjects) {
        int timeEstimate = timeCalculationService.calculateProjectTimeEstimateFromSubProjects(subProjects);
        int timeSpent = timeCalculationService.calculateProjectTotalTimeSpentFromSubProjects(subProjects);
        project.setProjectTimeEstimate(timeEstimate);
        project.setProjectTimeSpent(timeSpent);
        updateProject(project);
    }
    
    //--------update project time when subproject changes----------
    public void updateTimeWhenSubProjectChanges(Project project, List<SubProject> subProjects) {
        calculateAndUpdateProjectTotalsFromSubProjects(project, subProjects);
    }
}

