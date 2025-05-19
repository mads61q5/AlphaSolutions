package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final TimeCalculationService timeCalculationService;

    public DashboardController(ProjectService projectService,
                               SubProjectService subProjectService,
                               TimeCalculationService timeCalculationService) {
        this.projectService = projectService;
        this.subProjectService = subProjectService;
        this.timeCalculationService = timeCalculationService;
    }

    @GetMapping
    public String showDashboard(Model model, HttpSession session) {
        List<Project> projects = projectService.getAllProjects();
        int totalProjects = projects.size();
        int totalTimeEstimate = 0;
        int totalTimeSpent = 0;

        for (Project project : projects) {
            List<SubProject> subProjects = subProjectService.getSubProjectsByProject(project.getProjectID());
            TimeSummary projectTimeSummary = timeCalculationService.calculateProjectTimeSummary(project.getProjectID(), subProjects);
            totalTimeEstimate += projectTimeSummary.getTotalTimeEstimate();
            totalTimeSpent += projectTimeSummary.getTotalTimeSpent();
        }

        model.addAttribute("projects", projects);
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalTimeEstimate", totalTimeEstimate);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        return "dashboard";
    }
}
