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
    //---------------calculating entire project time summary-----------
    //------task time estimate
    public TimeSummary calculateProjectTimeSummary(int projectID, List<Task> tasks, List<SubProject> subProjects) {
        int taskTimeEstimate = 0;
        for (Task task : tasks) {
            taskTimeEstimate += task.getTaskTimeEstimate();
        }
//------------ sub project time estimate
        int subProjectTimeEstimate = 0;
        for (SubProject subProject : subProjects) {
            subProjectTimeEstimate += subProject.getSubProjectTimeEstimate();
        }

        int totalTimeEstimate = taskTimeEstimate + subProjectTimeEstimate;
//------------ task time spent
        int taskTimeSpent = 0;
        for (Task task : tasks) {
            taskTimeSpent += task.getTaskTimeSpent();
        }
//------------ sub project time spent
        int subProjectTimeSpent = 0;
        for (SubProject subProject : subProjects) {
            subProjectTimeSpent += subProject.getSubProjectTimeSpent();
        }
        int totalTimeSpent = taskTimeSpent + subProjectTimeSpent;

        return new TimeSummary(totalTimeEstimate, totalTimeSpent, taskTimeEstimate, taskTimeSpent, subProjectTimeEstimate, subProjectTimeSpent,  true);
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

        return new TimeSummary(taskTimeEstimate,taskTimeSpent,taskTimeEstimate,taskTimeSpent,0,0,onTrack);
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

        return new TimeSummary(subProjectTimeEstimate, subProjectTimeSpent, 0, 0,subProjectTimeEstimate, subProjectTimeSpent,onTrack);
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

        if (actualProgress >= expectedProgress) {
            return true;
        } else {
            return false;
        }
    }
}
