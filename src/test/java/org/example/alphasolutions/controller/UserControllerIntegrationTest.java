package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    void testUserRegistration() throws Exception {
        mockMvc.perform(post("/createUser")
                .param("username", "ChristianVinther")
                .param("password", "CVD2341")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        User createdUser = userService.getUserByName("ChristianVinther");
        assertNotNull(createdUser, "User should be created and found in the database");
        assertEquals("ChristianVinther", createdUser.getUserName()); 
        assertEquals("Employee", createdUser.getUserRole());
   
    }
}