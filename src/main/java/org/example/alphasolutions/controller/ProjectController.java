package org.example.alphasolutions.controller;

import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
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
    private final SubProjectService subProjectService;

    public ProjectController(ProjectService projectService, TimeCalculationService timeCalculationService,TaskService taskService, SubProjectService subProjectService) {
        this.projectService = projectService;
        this.timeCalculationService = timeCalculationService;
        this.subProjectService = subProjectService;
    }

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("username") != null;
    }

    //----------- get all projects
    @GetMapping
    public String getAllProjects(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/list";
    }
//---------- get project by ID
    @GetMapping("/{projectID}")
    public String getProjectByID(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectByID(projectID);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);

        TimeSummary timeSummary = timeCalculationService.calculateProjectTimeSummary(project, subProjects);
        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("timeSummary",timeSummary);
        return "projects/view";
    }
//------------ create new project (fill out form)
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = new Project();
        project.setProjectTimeEstimate(0); 
        model.addAttribute("project", project);
        return "projects/create";
    }
    //-------------create project

    @PostMapping
    public String createProject(@ModelAttribute Project project, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        project.setProjectTimeEstimate(0);
        projectService.createProject(project);
        return "redirect:/projects/" + project.getProjectID();
    }
//----------------edit project (edit form fill out)
    @GetMapping("/edit/{projectID}")
    public String showEditForm(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectByID(projectID);
        
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);
        int calculatedTimeEstimate = timeCalculationService.calculateProjectTimeEstimateFromSubProjects(subProjects);
        project.setProjectTimeEstimate(calculatedTimeEstimate);
        
        model.addAttribute("project", project);
        model.addAttribute("calculatedTimeEstimate", calculatedTimeEstimate); 
        return "projects/edit";
    }
//------------- update project (this should be a button which 'saves' the project edit)
    @PostMapping("/update")
    public String updateProject(@ModelAttribute Project project, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        Project currentProject = projectService.getProjectByID(project.getProjectID());
        
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(project.getProjectID());
        int calculatedTimeEstimate = timeCalculationService.calculateProjectTimeEstimateFromSubProjects(subProjects);
        
        project.setProjectTimeEstimate(calculatedTimeEstimate);
        
        projectService.updateProject(project);
        return "redirect:/projects/" + project.getProjectID();
    }

    //------------delete project by projectID
    @GetMapping("/delete/{projectID}")
    public String deleteProject(@PathVariable int projectID, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        projectService.deleteProject(projectID);
        return "redirect:/projects";
    }
}