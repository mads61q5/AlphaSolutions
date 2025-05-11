package org.example.alphasolutions.controller;
import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.TimeCalculationService;
import org.springframework.ui.Model;
import org.example.alphasolutions.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping ("/dashboard")
public class DashboardController {
    private final ProjectService projectService;
    private final TimeCalculationService timeCalculationService;

    public DashboardController(ProjectService projectService, TimeCalculationService timeCalculationService) {
        this.projectService = projectService;
        this.timeCalculationService = timeCalculationService;
    }
    @GetMapping
    public String showDashboard(Model model, HttpSession session) {

        List<Project> projects = projectService.getAllProjects();

        int totalProjects = projects.size();
        int totalTimeEstimate = 0;
        int totalTimeSpent = 0;
       /*nt totalTaskEstimate = 0;
        int totalTaskSpent = 0;
        int totalSubProjectEstimate = 0;
        int totalSubProjectSpent = 0;
        */

        for (Project project : projects) {
            totalTimeEstimate += project.getProjectTimeEstimate();
            totalTimeSpent += project.getProjectTimeSpent();
        }
        model.addAttribute("project",projects);
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalTimeEstimate", totalTimeEstimate);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        return "dashboard";
    }
}
