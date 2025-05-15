package org.example.alphasolutions.service;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.springframework.stereotype.Service;
import org.example.alphasolutions.model.TimeSummary;
import java.time.LocalDate;
import java.util.List;

@Service
public class TimeCalculationService {
    private TaskService taskService;
    private SubProjectService subProjectService;
    
    public TimeCalculationService(TaskService taskService, SubProjectService subProjectService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
    }
    
    //---------------calculating entire project time summary-----------
    public TimeSummary calculateProjectTimeSummary(Project project, List<SubProject> subProjects) {
        int totalTimeEstimate = 0;
        int totalTimeSpent = 0;
      //  int totalTaskTimeEstimate = 0;
        //int totalTaskTimeSpent = 0;
        
        // Calculate time for each subproject and its tasks
        for (SubProject subProject : subProjects) {
            TimeSummary subProjectSummary = calculateSubProjectTimeSummary(subProject);
            totalTimeEstimate += subProjectSummary.getTotalTimeEstimate();
            totalTimeSpent += subProjectSummary.getTotalTimeSpent();
          //  totalTaskTimeEstimate += subProjectSummary.getTaskTimeEstimate();
            //totalTaskTimeSpent += subProjectSummary.getTaskTimeSpent();
        }
        boolean onTrack = basicCalculateOnTrackStatus(project.getProjectStartDate(),
                                                      project.getProjectDeadline(),
                                                      totalTimeEstimate,
                                                      totalTimeSpent);
        
        
        return new TimeSummary(totalTimeEstimate, totalTimeSpent, 0, 0, 0,0,onTrack);
        // totalTaskTimeEstimate, totalTaskTimeSpent
    }
    
    
    //------------ sub project time summary ------------------------
    public TimeSummary calculateSubProjectTimeSummary(SubProject subProject) {
        List<Task> tasks = taskService.getTasksBySubProjectID(subProject.getSubProjectID());
        int taskTimeEstimate = 0;
        int taskTimeSpent = 0;
        
        
        for (Task task : tasks) {
            taskTimeEstimate += task.getTaskTimeEstimate();
            taskTimeSpent += task.getTaskTimeSpent();
        }
        boolean onTrack = basicCalculateOnTrackStatus(
                subProject.getSubProjectStartDate(),
                subProject.getSubProjectDeadline(),
                taskTimeEstimate,
                taskTimeSpent);
        return new TimeSummary(taskTimeEstimate, taskTimeSpent, 0, 0, 0, 0, true);
    }
    
    //---------------calculating task time summary-----------
    public TimeSummary calculateTasksTimeSummary(List<Task> tasks, SubProject subProject) {
        int taskTimeEstimate = 0;
        int taskTimeSpent = 0;
        
        for (Task task : tasks) {
            taskTimeEstimate += task.getTaskTimeEstimate();
            taskTimeSpent += task.getTaskTimeSpent();
        }
        boolean onTrack = basicCalculateOnTrackStatus(
                subProject.getSubProjectStartDate(),
                subProject.getSubProjectDeadline(),
                taskTimeEstimate,
                taskTimeSpent
        );
       // boolean onTrack = basicCalculateOnTrackStatus(subProject, taskTimeSpent);
        return new TimeSummary(taskTimeEstimate, taskTimeSpent, 0, 0, 0, 0, onTrack);
    }
    
    //---------------basic calculate on track status-----------
    private boolean basicCalculateOnTrackStatus(LocalDate startDate, LocalDate deadline, int totalTimeEstimate, int totalTimeSpent) {
        LocalDate today = LocalDate.now();
        //LocalDate deadline = project.getSubProjectDeadline();
        //LocalDate startDate = project.getSubProjectStartDate();
        
        if (today.isAfter(deadline)) {
            return false;
        }
        
        long totalDays = startDate.until(deadline).getDays();
        long daysPassed = startDate.until(today).getDays();
        
        if (daysPassed <= 0 || totalTimeEstimate == 0) {
            return true;
        }
        double expectedProgress = (double) daysPassed / (double) totalDays;
        double actualProgress = (double) totalTimeSpent / (double) totalTimeEstimate;
        
        return actualProgress >= expectedProgress;
        
       /* int totalTimeEstimate = calculateSubProjectTimeSummary(project).getTotalTimeEstimate();
        if (totalTimeEstimate == 0) return true;
        
        double expectedProgress = (double) daysPassed / (double) totalDays;
        double actualProgress = (double) timeSpent / (double) totalTimeEstimate;
        
        return actualProgress >= expectedProgress;
        
        */
    }

    
}