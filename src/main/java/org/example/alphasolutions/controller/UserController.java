package org.example.alphasolutions.controller;


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
    public String showLogin() {
        return "login";
    }

    @PostMapping ("login")
    public String login(@RequestParam ("username") String username,@RequestParam ("password") String password,
                        HttpSession session, Model model) {
        if(userService.login(username, password)) {
        session.setAttribute("user", username);
        session.setMaxInactiveInterval(600);
        return "redirect:/dashboard";
        }
        model.addAttribute("wrongCredentials", true);
        return "login";
    }
}
