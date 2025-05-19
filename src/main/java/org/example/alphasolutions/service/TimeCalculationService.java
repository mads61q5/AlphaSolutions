package org.example.alphasolutions.service;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.model.TimeSummary;
import org.springframework.stereotype.Service;

@Service
public class TimeCalculationService {

    //---------------calculating entire project time summary-----------
    public TimeSummary calculateProjectTimeSummary(Project project, List<SubProject> subProjects) {
        int calculatedProjectEstimate = 0;
        int calculatedProjectSpent = 0;

        for (SubProject sp : subProjects) {
            calculatedProjectEstimate += sp.getSubProjectTimeEstimate();
            calculatedProjectSpent += sp.getSubProjectTimeSpent();
        }

        boolean onTrack = basicCalculateOnTrackStatus(project, calculatedProjectSpent);

        return new TimeSummary(
            calculatedProjectEstimate, // totalTimeEstimate for the project
            calculatedProjectSpent,    // totalTimeSpent for the project
            calculatedProjectEstimate, // taskTimeEstimate (representing sum of all tasks via subprojects)
            calculatedProjectSpent,    // taskTimeSpent (representing sum of all tasks via subprojects)
            calculatedProjectEstimate, // subProjectTimeEstimate (representing sum of subproject contributions to project total)
            calculatedProjectSpent,    // subProjectTimeSpent (representing sum of subproject contributions to project total)
            onTrack
        );
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

        return new TimeSummary(taskTimeEstimate, taskTimeSpent, taskTimeEstimate, taskTimeSpent, 0, 0, onTrack);
    }

    //---------------calculating sub project time summary-----------
    public TimeSummary calculateSubProjectsTimeSummary(List<SubProject> subProjects, Project project) {
        int subProjectTimeEstimate = 0;
        int subProjectTimeSpent = 0;

        for (SubProject subProject : subProjects) {
            subProjectTimeEstimate += subProject.getSubProjectTimeEstimate();
            subProjectTimeSpent += subProject.getSubProjectTimeSpent();
        }

        boolean onTrack = basicCalculateOnTrackStatus(project, subProjectTimeSpent);

        return new TimeSummary(subProjectTimeEstimate, subProjectTimeSpent, 0, 0,
                               subProjectTimeEstimate, subProjectTimeSpent, onTrack);
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
    
    //---------------calculate project total time spent from subprojects-----------
    public int calculateProjectTotalTimeSpentFromSubProjects(List<SubProject> subProjects) {
        int totalTimeSpent = 0;
        for (SubProject subProject : subProjects) {
            totalTimeSpent += subProject.getSubProjectTimeSpent();
        }
        return totalTimeSpent;
    }
    
    //---------------calculate subproject time from tasks-----------
    public int calculateSubProjectTimeEstimateFromTasks(List<Task> tasks) {
        int totalTimeEstimate = 0;
        for (Task task : tasks) {
            totalTimeEstimate += task.getTaskTimeEstimate();
        }
        return totalTimeEstimate;
    }
    
    //---------------calculate project time from subprojects-----------
    public int calculateProjectTimeEstimateFromSubProjects(List<SubProject> subProjects) {
        int totalTimeEstimate = 0;
        for (SubProject subProject : subProjects) {
            totalTimeEstimate += subProject.getSubProjectTimeEstimate();
        }
        return totalTimeEstimate;
    }
    
    //---------------update time estimate propagation-----------
    public void updateTaskTimeAndPropagate(Task task, SubProject subProject, ProjectService projectService) {
        List<Task> tasks = subProject.getTasks();
        
        int subProjectTimeEstimate = calculateSubProjectTimeEstimateFromTasks(tasks);
        subProject.setSubProjectTimeEstimate(subProjectTimeEstimate);
        
        Project project = projectService.getProjectByID(subProject.getProjectID());
        if (project != null) {
            List<SubProject> subProjects = project.getSubProjects();
            int projectTimeEstimate = calculateProjectTimeEstimateFromSubProjects(subProjects);
            project.setProjectTimeEstimate(projectTimeEstimate);
            
            projectService.updateProject(project);
        }
    }
}