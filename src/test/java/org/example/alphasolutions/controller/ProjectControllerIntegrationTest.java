package org.example.alphasolutions.controller;

import java.time.LocalDate;

import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.UserService;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
@Transactional
public class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void testCreateProject() throws Exception {
        User sessionUser = userService.getUserByName("admin_user");
        assertNotNull(sessionUser, "Admin user for session should exist in test data.");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", sessionUser.getUserName());

        String projectName = "Integration Test Projekt 1";
        String projectDescription = "beskrivelse af projekt 1";
        String startDate = LocalDate.now().toString();
        String deadline = LocalDate.now().plusMonths(1).toString();
        String status = "NOT_STARTED";
        String priority = "HIGH";
        int timeEstimate = 100;
        int timeSpent = 0;

        mockMvc.perform(post("/projects")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("projectName", projectName)
                .param("projectDescription", projectDescription)
                .param("projectStartDate", startDate)
                .param("projectDeadline", deadline)
                .param("projectTimeEstimate", String.valueOf(timeEstimate))
                .param("projectTimeSpent", String.valueOf(timeSpent))
                .param("projectStatus", status)
                .param("projectPriority", priority))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/projects/*"));  
    }
}