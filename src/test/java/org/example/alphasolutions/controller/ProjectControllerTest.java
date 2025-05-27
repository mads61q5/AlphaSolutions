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
    public void givenUserIsNotAuthenticated_whenGetProjects_thenRedirectsToLogin() throws Exception {

        mockMvc.perform(get("/projects"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(timeCalculationService);
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