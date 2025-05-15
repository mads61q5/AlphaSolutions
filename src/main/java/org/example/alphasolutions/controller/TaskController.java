package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectID}/subprojects/{subProjectID}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final TimeCalculationService timeCalculationService;

    public TaskController(TaskService taskService, ProjectService projectService,
                          SubProjectService subProjectService,
                          TimeCalculationService timeCalculationService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
        this.timeCalculationService = timeCalculationService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    //-------------get all tasks-----------
    @GetMapping
    public String getAllTasks(
            @PathVariable int projectID,
            @PathVariable int subProjectID,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            Model model,
            HttpSession session
    ) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        
        List<Task> tasks;
        if (priority != null && !priority.isEmpty()) {
            tasks = taskService.getTasksByPriority(subProjectID, priority);
        } else if (status != null && !status.isEmpty()) {
            tasks = taskService.getTasksByStatus(subProjectID, status);
        } else {
            tasks = taskService.getTasksBySubProjectID(subProjectID);
        }
        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);
        
        TimeSummary timeSummary = timeCalculationService.calculateSubProjectTimeSummary(subProject);
      
        model.addAttribute("tasks", tasks);
        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);
        model.addAttribute("totalTimeEstimate", timeSummary.getTotalTimeEstimate());
        model.addAttribute("totalTimeSpent", timeSummary.getTotalTimeSpent());
        model.addAttribute("statuses", List.of("NOT_STARTED", "IN_PROGRESS", "COMPLETE"));
        model.addAttribute("currentStatus", status);
       // model.addAttribute("priority",List.of("HIGH", "MID", "LOW"));
        model.addAttribute("priority", priority);
        
        return "projects/tasks/list";
    }

    //------------Task time summary--------------------------
    @GetMapping("/time-summary")
    public String getTaskTimeSummary(@PathVariable int projectID, @PathVariable int subProjectID,
                                     Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Task> tasks = taskService.getTasksBySubProjectID(subProjectID);
        Project project = projectService.getProjectByID(projectID);
        SubProject subProject = subProjectService.getSubProjectByID(subProjectID);

        TimeSummary timeSummary = timeCalculationService.calculateTasksTimeSummary(tasks, subProject);
        model.addAttribute("project", project);
        model.addAttribute("subProject", subProject);
        model.addAttribute("timeSummary", timeSummary);

        return "projects/tasks/time-summary";
    }

    //--------------create new task (fill out form)-----------------
    @GetMapping("/new")
    public String showCreateForm(@PathVariable int projectID, @PathVariable int subProjectID,
                                 Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = new Task();
        task.setSubProjectID(subProjectID);

        model.addAttribute("task", task);
        model.addAttribute("project", projectService.getProjectByID(projectID));
        model.addAttribute("subProject", subProjectService.getSubProjectByID(subProjectID));
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
        return "redirect:/projects/" + projectID + "/subprojects/" + subProjectID + "/tasks";
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
        model.addAttribute("task", task);
        model.addAttribute("project", projectService.getProjectByID(projectID));
        model.addAttribute("subProject", subProjectService.getSubProjectByID(subProjectID));

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
}