package org.example.alphasolutions.service;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeCalculationService {

    public TimeSummary calculateProjectTimeSummary(int projectID, List<Task> tasks, List<SubProject> subProjects) {
        int totalTaskTime = tasks.stream().mapToInt(Task::getTaskTimeEstimate).sum();
        int totalSubProjectTime = subProjects.stream().mapToInt(SubProject::getSubProjectTimeEstimate).sum();
        int totalEstimated = totalTaskTime + totalSubProjectTime;

        int totalTaskTimeSpent = tasks.stream().mapToInt(Task::getTaskTimeSpent).sum();
        int totalSubProjectTimeSpent = subProjects.stream().mapToInt(SubProject::getSubProjectTimeSpent).sum();
        int totalTimeSpent = totalTaskTimeSpent + totalSubProjectTimeSpent;

        return new TimeSummary(totalEstimated, totalTimeSpent, totalTaskTime, totalSubProjectTime, true);
    }

    public TimeSummary calculateTasksTimeSummary(List<Task> tasks, Project project) {
        int totalEstimated = tasks.stream().mapToInt(Task::getTaskTimeEstimate).sum();
        int totalSpent = tasks.stream().mapToInt(Task::getTaskTimeSpent).sum();
        boolean onTrack = calculateOnTrackStatus(project, totalSpent);

        return new TimeSummary(totalEstimated, totalSpent, totalEstimated, 0, onTrack);
    }

    public TimeSummary calculateSubProjectsTimeSummary(List<SubProject> subProjects, Project project) {
        int totalEstimated = subProjects.stream().mapToInt(SubProject::getSubProjectTimeEstimate).sum();
        int totalSpent = subProjects.stream().mapToInt(SubProject::getSubProjectTimeSpent).sum();
        boolean onTrack = calculateOnTrackStatus(project, totalSpent);

        return new TimeSummary(totalEstimated, totalSpent, 0, totalEstimated, onTrack);
    }

    private boolean calculateOnTrackStatus(Project project, int timeSpent) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = project.getProjectDeadline();
        LocalDate startDate = project.getProjectStartDate();

        if (today.isAfter(deadline)) {
            return false; // Overdue
        }

        long totalDays = startDate.until(deadline).getDays();
        long daysPassed = startDate.until(today).getDays();

        if (daysPassed <= 0) {
            return true; // Project just started
        }

        double expectedProgress = (double) daysPassed / totalDays;
        double actualProgress = (double) timeSpent / project.getProjectTimeEstimate();

        return actualProgress >= expectedProgress;
    }

    public static class TimeSummary {
        private final int totalEstimated;
        private final int timeSpent;
        private final int taskTime;
        private final int subProjectTime;
        private final boolean onTrack;

        public TimeSummary(int totalEstimated, int timeSpent,
                           int taskTime, int subProjectTime,
                           boolean onTrack) {
            this.totalEstimated = totalEstimated;
            this.timeSpent = timeSpent;
            this.taskTime = taskTime;
            this.subProjectTime = subProjectTime;
            this.onTrack = onTrack;
        }

        // Getters
        public int getTotalEstimated() {
            return totalEstimated;
        }

        public int getTimeSpent() {
            return timeSpent;
        }

        public int getTaskTime() {
            return taskTime;
        }

        public int getSubProjectTime() {
            return subProjectTime;
        }

        public boolean isOnTrack() {
            return onTrack;
        }
    }
}
