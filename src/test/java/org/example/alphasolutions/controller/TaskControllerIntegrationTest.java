package org.example.alphasolutions.controller;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SubProjectService subProjectService;

    @Autowired
    private UserService userService;

    private final int testSubProjectId = 2;
    private final int testProjectId = 1;

    @Test
    void testAddTaskToSubProject() throws Exception {
        User sessionUser = userService.getUserByName("admin_user");
        assertNotNull(sessionUser, "Admin user for session should exist in test data.");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", sessionUser.getUserName());

        String taskName = "New UI Test Task";
        String taskDescription = "Description for new UI test task.";
        String startDate = LocalDate.now().toString();
        String deadline = LocalDate.now().plusDays(10).toString();

        mockMvc.perform(post("/projects/" + testProjectId + "/subprojects/" + testSubProjectId + "/tasks")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("taskName", taskName)
                .param("taskDescription", taskDescription)
                .param("taskStartDate", startDate)
                .param("taskDeadline", deadline)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/" + testProjectId + "/subprojects/" + testSubProjectId));

        List<Task> tasksInSubProject = taskService.getTasksBySubProjectID(testSubProjectId);
        assertNotNull(tasksInSubProject);
        assertFalse(tasksInSubProject.isEmpty(), "Task list for subproject should not be empty after adding a task");

        Task createdTask = tasksInSubProject.stream()
                .filter(t -> taskName.equals(t.getTaskName()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdTask, "Newly created task should be found in the subproject's task list");
        assertEquals(taskDescription, createdTask.getTaskDescription());
        assertEquals(testSubProjectId, createdTask.getSubProjectID());
    }
} 