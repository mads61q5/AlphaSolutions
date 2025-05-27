package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenValidCredentials_whenLogin_thenRedirectsToDashboardAndSetsSession() throws Exception {
        when(userService.login("testuser", "password")).thenReturn(true);

        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(request().sessionAttribute("username", "testuser"));

        verify(userService).login("testuser", "password");
    }

    @Test
    public void givenInvalidCredentials_whenLogin_thenReturnsLoginViewWithError() throws Exception {
        when(userService.login("testuser", "wrongpassword")).thenReturn(false);

        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "wrongpassword"))
                .andExpect(status().isOk()) 
                .andExpect(view().name("login"))
                .andExpect(model().attribute("wrongCredentials", true))
                .andExpect(request().sessionAttributeDoesNotExist("username"));

        verify(userService).login("testuser", "wrongpassword");
    }

    @Test
    public void whenShowCreateUserForm_thenReturnsCreateUserView() throws Exception {

        mockMvc.perform(get("/createUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"));
    }

    @Test
    public void givenNewUserDetails_whenCreateUser_thenRedirectsToLogin() throws Exception {
        doNothing().when(userService).saveUser(any(User.class));

        mockMvc.perform(post("/createUser")
                .param("username", "newuser")
                .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).saveUser(any(User.class)); 
    }

    @Test
    public void whenLogout_thenInvalidatesSessionAndRedirectsToLogin() throws Exception {

        mockMvc.perform(get("/logout")
                .sessionAttr("username", "testuser")) 
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(request().sessionAttributeDoesNotExist("username"));
    }

} 