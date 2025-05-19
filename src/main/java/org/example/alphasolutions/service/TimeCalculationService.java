package org.example.alphasolutions.service;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.springframework.stereotype.Service;
import org.example.alphasolutions.model.TimeSummary;
import java.time.LocalDate;
import java.util.List;

@Service
public class TimeCalculationService
{
    //---------------calculating entire project time summary-----------
    public TimeSummary calculateProjectTimeSummary(Project project, List<SubProject> subProjects) {
        int totalTimeEstimate = 0;
        int totalTimeSpent = 0;
        int totalTaskTimeEstimate = 0;
        int totalTaskTimeSpent = 0;
        
        for (SubProject subProject : subProjects) {
            TimeSummary subProjectSummary = calculateSubProjectTimeSummary(subProject);
            totalTimeEstimate += subProjectSummary.getTotalTimeEstimate();
            totalTimeSpent += subProjectSummary.getTotalTimeSpent();
            totalTaskTimeEstimate += subProjectSummary.getTaskTimeEstimate();
            totalTaskTimeSpent += subProjectSummary.getTaskTimeSpent();
        }
        
        boolean onTrack = basicCalculateOnTrackStatus(project, totalTaskTimeSpent);
        
        return new TimeSummary(totalTimeEstimate, totalTimeSpent, totalTaskTimeEstimate, totalTaskTimeSpent,
                               0, 0, onTrack);
        }
        
        //------------ sub project time summary
        public TimeSummary calculateSubProjectTimeSummary(SubProject subProject) {
            int taskTimeEstimate = 0;
            int taskTimeSpent = 0;
            
            List<Task> tasks = subProject.getTasks();
            for (Task task : tasks) {
                taskTimeEstimate += task.getTaskTimeEstimate();
                taskTimeSpent += task.getTaskTimeSpent();
            }
            return new TimeSummary(taskTimeEstimate, taskTimeSpent, taskTimeEstimate,
                    taskTimeSpent, 0, 0, true);
        }
    //---------------calculating task time summary-----------
    public TimeSummary calculateTasksTimeSummary(List<Task> tasks, Project project) {
        int taskTimeEstimate = 0;
        int taskTimeSpent = 0;
        
        for (Task task : tasks) {
            taskTimeEstimate += task.getTaskTimeEstimate();
            taskTimeSpent += task.getTaskTimeSpent();
        }
        
        boolean onTrack = basicCalculateOnTrackStatus(project, taskTimeSpent);
        
        return new TimeSummary(taskTimeEstimate, taskTimeSpent, taskTimeEstimate,
                taskTimeSpent, 0, 0, onTrack);
        
    }
    //---------------basic calculate on track status-----------
    private boolean basicCalculateOnTrackStatus(Project project, int timeSpent) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = project.getProjectDeadline();
        LocalDate startDate = project.getProjectStartDate();
        
        if (today.isAfter(deadline)) {
            return false;
        }
        long totalDays = startDate.until(deadline).getDays();
        long daysPassed = startDate.until(today).getDays();
        
        if (daysPassed <= 0) {
            return true;
        }
        double expectedProgress = (double) daysPassed / (double) totalDays;
        double actualProgress = (double) timeSpent / (double) project.getProjectTimeEstimate();
        
        return actualProgress >= expectedProgress;
    }
}