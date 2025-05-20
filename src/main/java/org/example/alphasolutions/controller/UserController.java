package org.example.alphasolutions.controller;


import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean isLogggedIn(HttpSession httpSession) {
        return httpSession.getAttribute("username") != null;
    }

    @GetMapping ("login")
    public String showLogin(HttpSession session) {
        if (isLogggedIn(session)) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping ("login")
    public String login(@RequestParam ("username") String username,@RequestParam ("password") String password,
                        HttpSession session, Model model) {
        if(userService.login(username, password)) {
        session.setAttribute("username", username);
        session.setMaxInactiveInterval(600);
        return "redirect:/dashboard";
        }
        model.addAttribute("wrongCredentials", true);
        return "login";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/createUser")
    public String showCreateUserForm(Model model) {
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             Model model) {
     
        User newUser = new User();
        newUser.setUserName(username);
        newUser.setUserPassword(password);
        newUser.setUserRole("Employee"); 
        userService.saveUser(newUser);
        return "redirect:/login";
    }
}
