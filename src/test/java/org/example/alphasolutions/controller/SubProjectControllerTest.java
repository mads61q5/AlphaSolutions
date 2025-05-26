package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.example.alphasolutions.service.UserService;
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

@WebMvcTest(SubProjectController.class)
public class SubProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private SubProjectService subProjectService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TimeCalculationService timeCalculationService;

    @MockBean
    private UserService userService;

    @Test
    public void givenUserIsAuthenticatedAndProjectId_whenGetSubProjects_thenReturnsSubProjectsListView() throws Exception {
        int projectId = 1;
        Project mockProject = new Project();
        mockProject.setProjectID(projectId);
        mockProject.setProjectName("Parent Project");

        SubProject subProject1 = new SubProject();
        subProject1.setSubProjectID(10);
        subProject1.setSubProjectName("Sub Project Alpha");
        SubProject subProject2 = new SubProject();
        subProject2.setSubProjectID(11);
        subProject2.setSubProjectName("Sub Project Beta");
        List<SubProject> mockSubProjects = List.of(subProject1, subProject2);

        when(projectService.getProjectByID(projectId)).thenReturn(mockProject);
        when(subProjectService.getSubProjectsByProject(projectId)).thenReturn(mockSubProjects);

        mockMvc.perform(get("/projects/{pid}/subprojects", projectId).sessionAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/subprojects/list"))
                .andExpect(model().attribute("project", mockProject))
                .andExpect(model().attribute("subProjects", mockSubProjects));

        verify(projectService).getProjectByID(projectId);
        verify(subProjectService).getSubProjectsByProject(projectId);
    }

    @Test
    public void givenUserIsNotAuthenticated_whenGetSubProjects_thenRedirectsToLogin() throws Exception {
        int projectId = 1;

        mockMvc.perform(get("/projects/{pid}/subprojects", projectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(taskService);
        verifyNoInteractions(timeCalculationService);
        verifyNoInteractions(userService);
    }

  
    @Test
    public void givenAuthUserAndIds_whenGetSubProjectByIdWithoutStatus_thenReturnsSubProjectDetailsViewWithAllTasks() throws Exception {
        int projectId = 1;
        int subProjectId = 10;

        Project mockProject = new Project();
        mockProject.setProjectID(projectId);

        SubProject mockSubProject = new SubProject();
        mockSubProject.setSubProjectID(subProjectId);
        mockSubProject.setSubProjectName("Detailed SubProject All Tasks");
        mockSubProject.setSubProjectStatus("IN PROGRESS");

        Task task1 = new Task();
        task1.setTaskID(100);
        task1.setTaskName("Task Alpha");
        task1.setTaskTimeEstimate(30);
        task1.setTaskTimeSpent(10);
        Task task2 = new Task();
        task2.setTaskID(101);
        task2.setTaskName("Task Omega");
        task2.setTaskTimeEstimate(40);
        task2.setTaskTimeSpent(15);
        List<Task> mockTasks = List.of(task1, task2);
        List<User> mockUsers = List.of(new User());

        int expectedTotalTaskTimeEstimate = 70; 
        int expectedTotalTaskTimeSpent = 25;

        TimeSummary mockTimeSummary = new TimeSummary(expectedTotalTaskTimeEstimate, expectedTotalTaskTimeSpent, expectedTotalTaskTimeEstimate, expectedTotalTaskTimeSpent, 0, 0, true);

        when(projectService.getProjectByID(projectId)).thenReturn(mockProject);
        when(subProjectService.getSubProjectByID(subProjectId)).thenReturn(mockSubProject);
        when(userService.getAllUsers()).thenReturn(mockUsers);
        when(taskService.getTasksBySubProjectID(subProjectId)).thenReturn(mockTasks);
        when(timeCalculationService.calculateTasksTimeSummary(mockTasks, mockProject)).thenReturn(mockTimeSummary);

        mockMvc.perform(get("/projects/{pid}/subprojects/{sid}", projectId, subProjectId)
                        .sessionAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects/subprojects/view"))
                .andExpect(model().attribute("project", mockProject))
                .andExpect(model().attribute("subProject", mockSubProject))
                .andExpect(model().attribute("tasks", mockTasks))
                .andExpect(model().attribute("timeSummary", mockTimeSummary))
                .andExpect(model().attribute("currentStatus", (String) null)) 
                .andExpect(model().attribute("taskTotalTimeEstimate", expectedTotalTaskTimeEstimate))
                .andExpect(model().attribute("taskTotalTimeSpent", expectedTotalTaskTimeSpent))
                .andExpect(model().attribute("allUsers", mockUsers));

        verify(projectService).getProjectByID(projectId);
        verify(subProjectService).getSubProjectByID(subProjectId);
        verify(userService).getAllUsers();
        verify(taskService).getTasksBySubProjectID(subProjectId);
        verify(timeCalculationService).calculateTasksTimeSummary(mockTasks, mockProject);
    }

    @Test
    public void givenUserIsNotAuthenticated_whenGetSubProjectById_thenRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 10;

        mockMvc.perform(get("/projects/{pid}/subprojects/{sid}", projectId, subProjectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectService);
        verifyNoInteractions(subProjectService);
        verifyNoInteractions(taskService);
        verifyNoInteractions(timeCalculationService);
        verifyNoInteractions(userService);
    }
} 