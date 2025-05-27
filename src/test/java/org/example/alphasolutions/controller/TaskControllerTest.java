package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.example.alphasolutions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private SubProjectService subProjectService;

    @MockBean
    private TimeCalculationService timeCalculationService;

    @MockBean
    private UserService userService;

    @Test
    public void givenUserIsAuthenticated_whenShowCreateForm_thenReturnsCreateTaskView() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        Project project = new Project();
        project.setProjectID(projectId);
        SubProject subProject = new SubProject();
        subProject.setSubProjectID(subProjectId);
        subProject.setProjectID(projectId);
        List<User> users = List.of(new User());

        when(projectService.getProjectByID(projectId)).thenReturn(project);
        when(subProjectService.getSubProjectByID(subProjectId)).thenReturn(subProject);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/projects/{projectID}/subprojects/{subProjectID}/tasks/new", projectId, subProjectId)
                .sessionAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/tasks/create"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attribute("project", project))
                .andExpect(model().attribute("subProject", subProject))
                .andExpect(model().attribute("users", users));

        verify(projectService).getProjectByID(projectId);
        verify(subProjectService).getSubProjectByID(subProjectId);
        verify(userService).getAllUsers();
    }

    @Test
    public void givenUserIsNotAuthenticated_whenShowCreateForm_thenRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;

        mockMvc.perform(get("/projects/{projectID}/subprojects/{subProjectID}/tasks/new", projectId, subProjectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(userService);
    }

    @Test
    public void givenUserIsAuthenticated_whenCreateTask_thenRedirectsToSubProjectView() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        Task taskToCreate = new Task();
        taskToCreate.setTaskName("New Task");

        SubProject subProject = new SubProject();
        subProject.setSubProjectID(subProjectId);
        Project project = new Project();
        project.setProjectID(projectId);

        when(subProjectService.getSubProjectByID(subProjectId)).thenReturn(subProject);
        when(taskService.getTasksBySubProjectID(subProjectId)).thenReturn(Collections.emptyList()); 
        doNothing().when(subProjectService).updateTimeWhenTaskChanges(any(SubProject.class), any(List.class));

        when(projectService.getProjectByID(projectId)).thenReturn(project);
        when(subProjectService.getSubProjectsByProject(projectId)).thenReturn(Collections.emptyList()); 
        doNothing().when(projectService).updateTimeWhenSubProjectChanges(any(Project.class), any(List.class));

        doNothing().when(taskService).createTask(any(Task.class));

        mockMvc.perform(post("/projects/{projectID}/subprojects/{subProjectID}/tasks", projectId, subProjectId)
                .sessionAttr("username", "testuser")
                .flashAttr("task", taskToCreate)) 
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/" + projectId + "/subprojects/" + subProjectId));

        verify(taskService).createTask(any(Task.class)); 

        verify(subProjectService).getSubProjectByID(subProjectId);
        verify(taskService).getTasksBySubProjectID(subProjectId);
        verify(subProjectService).updateTimeWhenTaskChanges(any(SubProject.class), any(List.class));
        verify(projectService).getProjectByID(projectId);
        verify(subProjectService).getSubProjectsByProject(projectId);
        verify(projectService).updateTimeWhenSubProjectChanges(any(Project.class), any(List.class));
    }

    @Test
    public void givenUserIsNotAuthenticated_whenCreateTask_thenRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        Task taskToCreate = new Task();
        taskToCreate.setTaskName("New Task");

        mockMvc.perform(post("/projects/{projectID}/subprojects/{subProjectID}/tasks", projectId, subProjectId)
                .flashAttr("task", taskToCreate))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(taskService);
        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
    }
} 