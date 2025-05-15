package org.example.alphasolutions.controller;

import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
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
@RequestMapping("/projects/{projectID}/subprojects/{subProjectID}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final TimeCalculationService timeCalculationService;
    private final UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService,
                          SubProjectService subProjectService,
                          TimeCalculationService timeCalculationService,
                          UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
        this.timeCalculationService = timeCalculationService;
        this.userService = userService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    //-------------get all tasks----------- (This method is no longer needed as tasks are shown on subproject view)
    /*
    @GetMapping
    public String getAllTasks(@PathVariable int projectID, @PathVariable int subProjectID,
                              @RequestParam(required = false) String status,
                              Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        List<Task> tasks;
        if (status != null && !status.isEmpty()) {
            tasks = taskService.getTasksByStatusAndSubProjectID(subProjectID, status); // Corrected method name
        } else {
            tasks = taskService.getTasksBySubProjectID(subProjectID);
        }

        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);

        int totalTimeEstimate = 0;
        int totalTimeSpent = 0;
        for (Task task : tasks) {
            totalTimeEstimate += task.getTaskTimeEstimate();
            totalTimeSpent += task.getTaskTimeSpent();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);
        model.addAttribute("totalTimeEstimate", totalTimeEstimate);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        model.addAttribute("statuses", List.of("NOT_STARTED", "IN_PROGRESS", "COMPLETE"));
        model.addAttribute("currentStatus", status);
        return "projects/tasks/list"; // This view will be deleted
    }
    */

    //------------Task time summary-------------------------- (This method might be removed if not used elsewhere)
    /*
    @GetMapping("/time-summary")
    public String getTaskTimeSummary(@PathVariable int projectID, @PathVariable int subProjectID,
                                     Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Task> tasks = taskService.getTasksBySubProjectID(subProjectID);
        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);

        TimeSummary timeSummary = timeCalculationService.calculateTasksTimeSummary(tasks, project);
        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);
        model.addAttribute("timeSummary", timeSummary);

        return "projects/tasks/time-summary"; // This view might also be deleted or functionality moved
    }
    */

    //--------------create new task (fill out form)-----------------
    @GetMapping("/new")
    public String showCreateForm(@PathVariable int projectID, @PathVariable int subProjectID,
                                 Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = new Task();
        task.setSubProjectID(subProjectID);
        List<User> users = userService.getAllUsers();

        model.addAttribute("task", task);
        model.addAttribute("project", projectService.getProjectByID(projectID));
        model.addAttribute("subProject", subProjectService.getSubProjectByID(subProjectID));
        model.addAttribute("users", users);
        return "projects/tasks/create";
    }

    //---------------- create new task------------------
    @PostMapping
    public String createTask(@PathVariable int projectID, @PathVariable int subProjectID,
                             @ModelAttribute Task task, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        task.setTaskStatus("NOT_STARTED");
        task.setTaskTimeSpent(0);
        task.setSubProjectID(subProjectID);

        taskService.createTask(task);
        return "redirect:/projects/" + projectID + "/subprojects/" + subProjectID;
    }

    //-------------get tasks by ID-----------
    @GetMapping("/{taskID}")
    public String getTaskByID(@PathVariable int projectID, @PathVariable int subProjectID,
                              @PathVariable int taskID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskByID(taskID);
        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);

        model.addAttribute("task", task);
        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);

        return "projects/tasks/view";
    }

    //---------------------edit task fill out form-------------------
    @GetMapping("/edit/{taskID}")
    public String showEditForm(@PathVariable int projectID, @PathVariable int subProjectID,
                               @PathVariable int taskID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskByID(taskID);
        List<User> users = userService.getAllUsers();
        model.addAttribute("task", task);
        model.addAttribute("project", projectService.getProjectByID(projectID));
        model.addAttribute("subProject", subProjectService.getSubProjectByID(subProjectID));
        model.addAttribute("users", users);

        return "projects/tasks/edit";
    }

    //-------------------update task---------------
    @PostMapping("/update")
    public String updateTask(@PathVariable int projectID, @PathVariable int subProjectID,
                             @ModelAttribute Task task, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        task.setSubProjectID(subProjectID);
        taskService.updateTask(task, task.getTaskID());

        return "redirect:/projects/" + projectID + "/subprojects/" + subProjectID + "/tasks/" + task.getTaskID();
    }

    //-----------------delete task-----------------------
    @GetMapping("/delete/{taskID}")
    public String deleteTask(@PathVariable int projectID, @PathVariable int subProjectID,
                             @PathVariable int taskID, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        taskService.deleteTask(taskID);
        return "redirect:/projects/" + projectID + "/subprojects/" + subProjectID + "/tasks";
    }

    // Endpoint to add hours to a task
    @PostMapping("/{taskID}/add-hours")
    public String addHoursToTask(@PathVariable int projectID, 
                                 @PathVariable int subProjectID, 
                                 @PathVariable int taskID, 
                                 @RequestParam("hours") int hours, 
                                 HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskByID(taskID);
        if (task != null) {
            task.setTaskTimeSpent(task.getTaskTimeSpent() + hours);
            taskService.updateTask(task, taskID);

            // Update the parent SubProject's timeSpent
            SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
            if (subProject != null) {
                subProject.setSubProjectTimeSpent(subProject.getSubProjectTimeSpent() + hours);
                // Assuming subProjectService.updateSubProject takes the subProject object and its ID
                // The existing updateSubProject method in SubProjectService is:
                // updateSubProject(SubProject subProject, int subProjectID)
                // The subProject object fetched above already has its ID.
                subProjectService.updateSubProject(subProject, subProject.getSubProjectID());
            }
        }
        // Redirect back to the subproject view page where the task list is displayed
        return "redirect:/projects/" + projectID + "/subprojects/" + subProjectID;
    }
}