package org.example.alphasolutions.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Task {
    private int taskID;
    private String taskName;
    private String taskDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate taskDeadline;
    private int taskTimeEstimate;
    private int taskTimeSpent;
    private String taskStatus;
    private String taskPriority;
    private int projectID;
    private int subProjectID;


    public Task(int taskId, String taskName, String taskDescription, LocalDate taskStartDate, LocalDate taskDeadline,
                int taskTimeEstimate, int taskTimeSpent, String taskStatus, String taskPriority, int projectId) {
    }

    public Task(int taskID, String taskName, String taskDescription,
                LocalDate taskStartDate, LocalDate taskDeadline,
                int taskTimeEstimate, int taskTimeSpent,
                String taskStatus, String taskPriority,
                int projectID, int subProjectID) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDeadline = taskDeadline;
        this.taskTimeEstimate = taskTimeEstimate;
        this.taskTimeSpent = taskTimeSpent;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.projectID = projectID;
        this.subProjectID = subProjectID;
    }

    //---------Getters and Setters
    //----------------Task ID----------------
    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    //----------------Task Name----------------
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //----------------Task Description----------------
    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    //----------------Task Start Date----------------
    public LocalDate getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(LocalDate taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    //----------------Task Deadline----------------
    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(LocalDate taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    //----------------Task Time Estimate----------------
    public int getTaskTimeEstimate() {
        return taskTimeEstimate;
    }

    public void setTaskTimeEstimate(int taskTimeEstimate) {
        this.taskTimeEstimate = taskTimeEstimate;
    }

    //----------------Task time spent----------------
    public int getTaskTimeSpent() {
        return taskTimeSpent;
    }

    public void setTaskTimeSpent(int taskTimeSpent) {
        this.taskTimeSpent = taskTimeSpent;
    }

    //----------------Task Status----------------
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    //----------------Task priority----------------
    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    //----------------Project ID----------------
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    //----------------Sub Project ID----------------
    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }
}
