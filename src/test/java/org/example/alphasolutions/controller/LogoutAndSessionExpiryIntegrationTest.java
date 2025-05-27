package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
class LogoutAndSessionExpiryIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    void logoutInvalidatesSessionAndProtectsDashboard() throws Exception {
        User admin = userService.getUserByName("admin_user");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", admin.getUserName());

        mockMvc.perform(get("/dashboard").session(session))
               .andExpect(status().isOk());

        mockMvc.perform(get("/logout").session(session))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"))
               .andExpect(request().sessionAttributeDoesNotExist("username"));

        mockMvc.perform(get("/dashboard").session(session))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"));
    }
}