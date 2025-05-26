package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import java.util.List;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private SubProjectService subProjectService;

    @MockBean
    private TimeCalculationService timeCalculationService;

    @Test
    public void givenUserIsAuthenticated_whenGetDashboard_thenReturnsDashboardViewWithData() throws Exception {
        Project project1 = new Project();
        project1.setProjectID(1);
        project1.setProjectName("Project Alpha");

        Project project2 = new Project();
        project2.setProjectID(2);
        project2.setProjectName("Project Beta");

        List<Project> mockProjects = List.of(project1, project2);
        List<SubProject> mockSubProjects1 = List.of(new SubProject(), new SubProject()); 
        List<SubProject> mockSubProjects2 = List.of(new SubProject()); 


        TimeSummary timeSummary1 = new TimeSummary(10, 5, 0, 0, 0, 0, true);
        TimeSummary timeSummary2 = new TimeSummary(20, 15, 0, 0, 0, 0, true);

        when(projectService.getAllProjects()).thenReturn(mockProjects);
        when(subProjectService.getSubProjectsByProject(1)).thenReturn(mockSubProjects1);
        when(subProjectService.getSubProjectsByProject(2)).thenReturn(mockSubProjects2);
        when(timeCalculationService.calculateProjectTimeSummary(project1, mockSubProjects1)).thenReturn(timeSummary1);
        when(timeCalculationService.calculateProjectTimeSummary(project2, mockSubProjects2)).thenReturn(timeSummary2);

        mockMvc.perform(get("/dashboard").sessionAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("projects", mockProjects))
                .andExpect(model().attribute("totalProjects", 2))
                .andExpect(model().attribute("totalTimeEstimate", 30))
                .andExpect(model().attribute("totalTimeSpent", 20));

        verify(projectService).getAllProjects();
        verify(subProjectService).getSubProjectsByProject(1);
        verify(subProjectService).getSubProjectsByProject(2);
        verify(timeCalculationService).calculateProjectTimeSummary(project1, mockSubProjects1);
        verify(timeCalculationService).calculateProjectTimeSummary(project2, mockSubProjects2);
    }

    @Test
    public void givenUserIsNotAuthenticated_whenGetDashboard_thenRedirectsToLogin() throws Exception {

        mockMvc.perform(get("/dashboard")) 
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(timeCalculationService);
    }
} 