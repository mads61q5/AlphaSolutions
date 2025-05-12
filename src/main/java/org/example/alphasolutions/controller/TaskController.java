package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.TimeSummary;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.TaskService;
import org.example.alphasolutions.service.TimeCalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectID}/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final TimeCalculationService timeCalculationService;

    public TaskController(TaskService taskService, ProjectService projectService,
                          TimeCalculationService timeCalculationService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.timeCalculationService = timeCalculationService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
//-------------get all tasks-----------
    @GetMapping
    public String getAllTasks(@PathVariable int projectID, int subProjectID,
                              @RequestParam(required = false) String status,
                              Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        List<Task> tasks;
        if (status != null && !status.isEmpty()) {
            tasks = taskService.getTasksBySubProjectID(subProjectID);
        } else {
            tasks = taskService.getTasksByProject(projectID);
        }
        Project project = projectService.getProjectByID(projectID);
        int totalTimeEstimate = 0;
        int totalTimeSpent = 0;
        for (Task task : tasks) {
            totalTimeEstimate += task.getTaskTimeEstimate();
            totalTimeSpent += task.getTaskTimeSpent();
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("project", project);
        model.addAttribute("totalTimeEstimate", totalTimeEstimate);
        model.addAttribute("totalTimeSpent", totalTimeSpent);
        model.addAttribute("statuses", List.of("NOT_STARTED", "IN_PROGRESS", "COMPLETE"));
        model.addAttribute("currentStatus", status);
        return "projects/tasks/list";
    }
//------------Task time summary--------------------------
    @GetMapping("/time-summary")
    public String getTaskTimeSummary(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Task> tasks = taskService.getTasksByProject(projectID);
        Project project = projectService.getProjectByID(projectID);

        TimeSummary timeSummary = timeCalculationService.calculateTasksTimeSummary(tasks, project);
        model.addAttribute("project", project);
        model.addAttribute("timeSummary", timeSummary);

        return "projects/task/time-summary";
    }
    //--------------create new task (fill out form)-----------------

    @GetMapping("/new")
    public String showCreateForm(@PathVariable int projectID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = new Task();
        task.setProjectID(projectID);

        model.addAttribute("task", task);
        model.addAttribute("project", projectService.getProjectByID(projectID));
        return "projects/tasks/create";
    }
//---------------- create new task------------------
    @PostMapping
    public String createTask(@PathVariable int projectID, @ModelAttribute Task task, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        task.setTaskStatus("NOT_STARTED");
        task.setTaskTimeSpent(0);

        taskService.createTask(task);
        return "redirect:/projects/" + projectID + "/tasks";
    }
    //-------------get tasks by ID-----------

    @GetMapping("/{taskID}")
    public String getTaskByID(@PathVariable int projectID, @PathVariable int taskID, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskByID(taskID);
        Project project = projectService.getProjectByID(projectID);
        model.addAttribute("task", task);
        model.addAttribute("project", project);

        return "projects/tasks/view";
    }
//---------------------edit task fill out form-------------------
    @GetMapping("/edit/{taskID}")
    public String showEditForm(@PathVariable int projectID, @PathVariable int taskID, Model model,
                               HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskByID(taskID);
        model.addAttribute("task", task);
        model.addAttribute("project", projectService.getProjectByID(projectID));

        return "projects/tasks/edit";
    }
    //-------------------update task---------------

    @PostMapping("/update")
    public String updateTask(@PathVariable int projectID, @PathVariable int taskID, @ModelAttribute Task task,
                             HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        task.setProjectID(projectID);
        taskService.updateTask(task, taskID);

        return "redirect:/projects" + projectID + "/tasks/" + task.getTaskID();
    }
//-----------------delete task-----------------------
    @GetMapping("/delete/{taskID}")
    public String deleteTask(@PathVariable int projectID, @PathVariable int taskID, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        taskService.deleteTask(taskID);
        return "redirect:/projects/" + projectID + "/tasks";

    }

}
