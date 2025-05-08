package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final TimeCalculationService timeCalculationService;

    public ProjectController(ProjectService projectService, TimeCalculationService timeCalculationService) {
        this.projectService = projectService;
        this.timeCalculationService = timeCalculationService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping
    public String getAllProjects(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/list";
    }

    @GetMapping("/{projectId}")
    public String getProjectById(@PathVariable int projectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        return "projects/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("project", new Project());
        return "projects/create";
    }

    @PostMapping
    public String createProject(@ModelAttribute Project project, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        projectService.createProject(project);
        return "redirect:/projects/" + project.getProjectID();
    }

    @GetMapping("/edit/{projectId}")
    public String showEditForm(@PathVariable int projectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("project", projectService.getProjectById(projectId));
        return "projects/edit";
    }

    @PostMapping("/update")
    public String updateProject(@ModelAttribute Project project, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        projectService.updateProject(project);
        return "redirect:/projects/" + project.getProjectID();
    }

    @GetMapping("/delete/{projectId}")
    public String deleteProject(@PathVariable int projectId, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        projectService.deleteProject(projectId);
        return "redirect:/projects";
    }
}