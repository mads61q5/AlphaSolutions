package org.example.alphasolutions.controller;

import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.model.User;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.example.alphasolutions.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/projects/{projectID}/subprojects")
public class SubProjectController {
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final TaskService taskService;
    private final TimeCalculationService timeCalculationService;
    private final UserService userService;

    public SubProjectController(ProjectService projectService,
                                SubProjectService subProjectService,
                                TaskService taskService,
                                TimeCalculationService timeCalculationService,
                                UserService userService) {
        this.projectService = projectService;
        this.subProjectService = subProjectService;
        this.taskService = taskService;
        this.timeCalculationService = timeCalculationService;
        this.userService = userService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    //----------- get all subprojects for a project
    @GetMapping
    public String getAllSubProjects(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectByID(projectID);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);
        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        return "projects/subprojects/list";
    }

    //---------- get subproject by ID (view details)
    @GetMapping("/{subProjectID}")
    public String getSubProjectById(@PathVariable int projectID, @PathVariable int subProjectID, 
                                    @RequestParam(required = false) String status,
                                    Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
        List<User> allUsers = userService.getAllUsers();

        List<Task> tasks;
        if (status != null && !status.isEmpty()) {
            tasks = taskService.getTasksByStatusAndSubProjectID(subProjectID, status);
        } else {
            tasks = taskService.getTasksBySubProjectID(subProjectID);
        }
        
        TimeSummary timeSummary = timeCalculationService.calculateTasksTimeSummary(tasks, project);
        
        int taskTotalTimeEstimate = tasks.stream().mapToInt(Task::getTaskTimeEstimate).sum();
        int taskTotalTimeSpent = tasks.stream().mapToInt(Task::getTaskTimeSpent).sum();

        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);
        model.addAttribute("tasks", tasks);
        model.addAttribute("timeSummary", timeSummary);
        model.addAttribute("statuses", List.of("NOT_STARTED", "IN_PROGRESS", "COMPLETE"));
        model.addAttribute("currentStatus", status);
        model.addAttribute("taskTotalTimeEstimate", taskTotalTimeEstimate);
        model.addAttribute("taskTotalTimeSpent", taskTotalTimeSpent);
        model.addAttribute("allUsers", allUsers);
        return "projects/subprojects/view";
    }

    //------------ show form to create a new subproject
    @GetMapping("/new")
    public String showCreateForm(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        SubProject newSubProject = new SubProject();
        newSubProject.setProjectID(projectID);  
        newSubProject.setSubProjectTimeEstimate(0); 
        model.addAttribute("subProject", newSubProject);
        model.addAttribute("project", projectService.getProjectByID(projectID));
        return "projects/subprojects/create";
    }

    //------------- create new subproject (form submission)
    @PostMapping
    public String createSubProject(@PathVariable int projectID, @ModelAttribute SubProject subProject, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        subProject.setProjectID(projectID);
        subProject.setSubProjectTimeEstimate(0);
        SubProject createdSubProject = subProjectService.createSubProject(subProject);
        
        Project project = projectService.getProjectByID(projectID);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);
        projectService.updateTimeWhenSubProjectChanges(project, subProjects);
        
        return "redirect:/projects/" + projectID + "/subprojects/" + createdSubProject.getSubProjectID();
    }

    //---------------- edit subproject (show edit form)
    @GetMapping("/edit/{subProjectID}")
    public String showEditForm(@PathVariable int projectID, @PathVariable int subProjectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
        
        List<Task> tasks = taskService.getTasksBySubProjectID(subProjectID);
        int calculatedTimeEstimate = timeCalculationService.calculateSubProjectTimeEstimateFromTasks(tasks);
        subProject.setSubProjectTimeEstimate(calculatedTimeEstimate);
        
        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);
        model.addAttribute("calculatedTimeEstimate", calculatedTimeEstimate); // Add for display only
        return "projects/subprojects/edit";
    }

    //----------------- update subproject (form submission)
    @PostMapping("/update")
    public String updateSubProject(@PathVariable int projectID, @ModelAttribute SubProject subProject, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        subProject.setProjectID(projectID);
        
        SubProject currentSubProject = subProjectService.getSubProjectByID(subProject.getSubProjectID());
        List<Task> tasks = taskService.getTasksBySubProjectID(subProject.getSubProjectID());
        int calculatedTimeEstimate = timeCalculationService.calculateSubProjectTimeEstimateFromTasks(tasks);
        
        subProject.setSubProjectTimeEstimate(calculatedTimeEstimate);
        
        subProjectService.updateSubProject(subProject, subProject.getSubProjectID());
        
        Project project = projectService.getProjectByID(projectID);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);
        projectService.updateTimeWhenSubProjectChanges(project, subProjects);
        
        return "redirect:/projects/" + projectID + "/subprojects/" + subProject.getSubProjectID();
    }

    //------------------ delete subproject
    @GetMapping("/delete/{subProjectID}")
    public String deleteSubProject(@PathVariable int projectID, @PathVariable int subProjectID, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        subProjectService.deleteSubProject(subProjectID);
        
        Project project = projectService.getProjectByID(projectID);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProject(projectID);
        projectService.updateTimeWhenSubProjectChanges(project, subProjects);
        
        return "redirect:/projects/" + projectID + "/subprojects";
    }
}