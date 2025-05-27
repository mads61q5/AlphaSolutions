package org.example.alphasolutions.controller;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
class TaskCreationUpdatesEstimatesIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserService userService;
    @Autowired TaskService taskService;
    @Autowired SubProjectService subProjectService;
    @Autowired ProjectService projectService;

    final int projectId   = 1;
    final int subProjectId = 2;

    @Test
    void creatingTaskUpdatesTimeEstimatesUpwards() throws Exception {
        User admin = userService.getUserByName("admin_user");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", admin.getUserName());

        SubProject beforeSub = subProjectService.getSubProjectByID(subProjectId);
        Project    beforePrj = projectService.getProjectByID(projectId);

        int estimateBeforeSub = beforeSub.getSubProjectTimeEstimate();
        int estimateBeforePrj = beforePrj.getProjectTimeEstimate();

        int newTaskEstimate = 12;

        mockMvc.perform(post("/projects/{pid}/subprojects/{sid}/tasks", projectId, subProjectId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("taskName", "US3 Task")
                        .param("taskDescription", "Automated test task")
                        .param("taskStartDate", LocalDate.now().toString())
                        .param("taskDeadline", LocalDate.now().plusDays(5).toString())
                        .param("taskTimeEstimate", String.valueOf(newTaskEstimate)))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/projects/" + projectId + "/subprojects/" + subProjectId));

        SubProject afterSub = subProjectService.getSubProjectByID(subProjectId);
        Project    afterPrj = projectService.getProjectByID(projectId);

        assertThat(afterSub.getSubProjectTimeEstimate())
                .as("Subprojektets estimat skal stige")
                .isEqualTo(estimateBeforeSub + newTaskEstimate);

        assertThat(afterPrj.getProjectTimeEstimate())
                .as("Projektets estimat skal stige tilsvarende")
                .isEqualTo(estimateBeforePrj + newTaskEstimate);
    }
}