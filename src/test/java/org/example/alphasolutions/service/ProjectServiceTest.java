package org.example.alphasolutions.service;

import java.util.List;

import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock 
    ProjectRepository projectRepository;  
    @Mock 
    TimeCalculationService timeCalcService;
    @InjectMocks 
    ProjectService projectService;  

    @Test 
    void getAllProjects_ReturnsAllProjectsFromRepository() {
        Project p1 = new Project(); p1.setProjectID(1); p1.setProjectName("Alpha");
        Project p2 = new Project(); p2.setProjectID(2); p2.setProjectName("Beta");
        List<Project> allProjects = List.of(p1, p2);
        when(projectRepository.findAll()).thenReturn(allProjects);

        List<Project> result = projectService.getAllProjects();

        assertEquals(allProjects, result);
        verify(projectRepository).findAll();
    }

    @Test 
    void calculateAndUpdateProjectTotalsFromSubProjects_UpdatesProjectAndCallsRepo() {
        Project project = new Project();
        project.setProjectID(100);
        SubProject sp1 = new SubProject(); sp1.setSubProjectTimeEstimate(40); sp1.setSubProjectTimeSpent(10);
        SubProject sp2 = new SubProject(); sp2.setSubProjectTimeEstimate(60); sp2.setSubProjectTimeSpent(20);
        List<SubProject> subProjects = List.of(sp1, sp2);
        when(timeCalcService.calculateProjectTimeEstimateFromSubProjects(subProjects)).thenReturn(100);
        when(timeCalcService.calculateProjectTotalTimeSpentFromSubProjects(subProjects)).thenReturn(30);

        projectService.calculateAndUpdateProjectTotalsFromSubProjects(project, subProjects);

        assertEquals(100, project.getProjectTimeEstimate());
        assertEquals(30, project.getProjectTimeSpent());
        verify(projectRepository).update(project);  
    }
}