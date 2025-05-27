package org.example.alphasolutions.service;

import org.example.alphasolutions.Interfaces.UserRepository;
import org.example.alphasolutions.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock 
    UserRepository userRepository;
    @InjectMocks 
    UserService userService;

    @Test 
    void login_WithCorrectCredentials_ReturnsTrue() {
        String username = "testuser";
        String password = "secret";
        User dummy = new User();
        dummy.setUserName(username);
        dummy.setUserPassword(password);
        when(userRepository.findByUserName(username)).thenReturn(dummy);

        assertTrue(userService.login(username, password));
        verify(userRepository).findByUserName(username);
    }

    @Test 
    void login_WithWrongPasswordOrUnknownUser_ReturnsFalse() {
        String username = "testuser";
        String correctPwd = "secret", wrongPwd = "guess";
        User dummy = new User(); dummy.setUserName(username); dummy.setUserPassword(correctPwd);
        when(userRepository.findByUserName(username)).thenReturn(dummy);
        assertFalse(userService.login(username, wrongPwd));  
        when(userRepository.findByUserName("nouser")).thenReturn(null);
        assertFalse(userService.login("nouser", "pass"));
    }

    @Test 
    void getUserByID_ReturnsUserFromRepository() {
        User u = new User(); u.setUserID(5);
        when(userRepository.findByID(5)).thenReturn(u);
        User result = userService.getUserByID(5);
        assertEquals(5, result.getUserID());
        verify(userRepository).findByID(5);
    }
}