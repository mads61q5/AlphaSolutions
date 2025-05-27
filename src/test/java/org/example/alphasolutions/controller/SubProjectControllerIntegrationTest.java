package org.example.alphasolutions.controller;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.UserService;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class SubProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubProjectService subProjectService;

    @Autowired
    private UserService userService;

    private final int testProjectId = 2;

    @Test
    void testAddSubProjectToProject() throws Exception {
        User sessionUser = userService.getUserByName("admin_user");
        assertNotNull(sessionUser, "Admin user for session should exist in test data.");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", sessionUser.getUserName());

        String subProjectName = "Et nyt Subproject";
        String subProjectDescription = "Beskrivelse af det nye subproject";
        String startDate = LocalDate.now().toString();
        String deadline = LocalDate.now().plusMonths(2).toString();

        mockMvc.perform(post("/projects/" + testProjectId + "/subprojects")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("subProjectName", subProjectName)
                .param("subProjectDescription", subProjectDescription)
                .param("subProjectStartDate", startDate)
                .param("subProjectDeadline", deadline))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/projects/" + testProjectId + "/subprojects/*"));

        List<SubProject> subProjectsInProject = subProjectService.getSubProjectsByProject(testProjectId);
        assertNotNull(subProjectsInProject);
        assertFalse(subProjectsInProject.isEmpty(), "Subproject list for project should not be empty after adding one.");

    }
}