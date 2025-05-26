package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.example.alphasolutions.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private SubProjectService subProjectService;

    @MockBean
    private TimeCalculationService timeCalculationService;

    @MockBean
    private TaskService taskService;

    @Test
    public void givenUserIsAuthenticated_whenGetProjects_thenReturnsProjectsListViewWithData() throws Exception {
        Project project1 = new Project();
        project1.setProjectID(1);
        project1.setProjectName("Project Gamma");

        Project project2 = new Project();
        project2.setProjectID(2);
        project2.setProjectName("Project Delta");

        List<Project> mockProjects = List.of(project1, project2);
        when(projectService.getAllProjects()).thenReturn(mockProjects);

        mockMvc.perform(get("/projects").sessionAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/list"))
                .andExpect(model().attribute("projects", mockProjects));

        verify(projectService).getAllProjects();
    }

    @Test
    public void givenUserIsNotAuthenticated_whenGetProjects_thenRedirectsToLogin() throws Exception {

        mockMvc.perform(get("/projects"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(timeCalculationService);
    }

    @Test
    public void givenUserIsAuthenticatedAndProjectId_whenGetProjectById_thenReturnsProjectDetailsView() throws Exception {
        int projectId = 1;
        Project mockProject = new Project();
        mockProject.setProjectID(projectId);
        mockProject.setProjectName("Test Project Detail");
        mockProject.setProjectStatus("IN PROGRESS");

        List<SubProject> mockSubProjects = List.of(new SubProject());
        TimeSummary mockTimeSummary = new TimeSummary(100, 50, 60,30, 40, 20, true);

        when(projectService.getProjectByID(projectId)).thenReturn(mockProject);
        when(subProjectService.getSubProjectsByProject(projectId)).thenReturn(mockSubProjects);
        when(timeCalculationService.calculateProjectTimeSummary(mockProject, mockSubProjects)).thenReturn(mockTimeSummary);

        mockMvc.perform(get("/projects/{id}", projectId).sessionAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/view"))
                .andExpect(model().attribute("project", mockProject))
                .andExpect(model().attribute("subProjects", mockSubProjects))
                .andExpect(model().attribute("timeSummary", mockTimeSummary));

        verify(projectService).getProjectByID(projectId);
        verify(subProjectService).getSubProjectsByProject(projectId);
        verify(timeCalculationService).calculateProjectTimeSummary(mockProject, mockSubProjects);
    }

    @Test
    public void givenUserIsNotAuthenticated_whenGetProjectById_thenRedirectsToLogin() throws Exception {
        int projectId = 1;

        mockMvc.perform(get("/projects/{id}", projectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(timeCalculationService);
    }

   
    @Test
    public void givenUserIsNotAuthenticated_whenPostProjects_thenRedirectsToLogin() throws Exception {

        mockMvc.perform(post("/projects")
                        .param("projectName", "Attempted Project Name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(timeCalculationService);
    }
} 