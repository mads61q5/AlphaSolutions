package org.example.alphasolutions.controller;

import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final TimeCalculationService timeCalculationService;
    private final TaskService taskService;
    private final SubProjectService subProjectService;

    public ProjectController(ProjectService projectService, TimeCalculationService timeCalculationService,TaskService taskService, SubProjectService subProjectService) {
        this.projectService = projectService;
        this.timeCalculationService = timeCalculationService;
        this.taskService = taskService;
        this.subProjectService = subProjectService;
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

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("project", new Project());
        return "projects/create";
    }

    @GetMapping("/{projectID}")
    public String getProjectById(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectById(projectID);
        List<Task> tasks = taskService.getTasksByProject(projectID);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);

        TimeSummary timeSummary = timeCalculationService.calculateProjectTimeSummary(project, tasks, subProjects);
        model.addAttribute("project", project);
        model.addAttribute("timeSummary",timeSummary);

        return "projects/view";
    }

    @PostMapping
    public String createProject(@ModelAttribute Project project, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project createdProject = projectService.createProject(project);
        return "redirect:/projects/" + createdProject.getProjectID();
    }

    @GetMapping("/edit/{projectID}")
    public String showEditForm(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("project", projectService.getProjectById(projectID));
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

    @GetMapping("/delete/{projectID}")
    public String deleteProject(@PathVariable int projectID, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        projectService.deleteProject(projectID);
        return "redirect:/projects";
    }
}