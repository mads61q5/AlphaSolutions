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
    private int taskDedicatedHours;
    private String taskStatus;
    private int assignedTaskUserID;
    private String assignedTaskUserName;

    public Task() {
    }

    public Task(int taskID, String taskName, String taskDescription,
                LocalDate taskStartDate, LocalDate taskDeadline,
                int taskTimeEstimate, int taskDedicatedHours,
                String taskStatus,  int assignedTaskUserID, String assignedTaskUserName) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDeadline = taskDeadline;
        this.taskTimeEstimate = taskTimeEstimate;
        this.taskDedicatedHours = taskDedicatedHours;
        this.taskStatus = taskStatus;
        this.assignedTaskUserID= assignedTaskUserID;
        this.assignedTaskUserName = assignedTaskUserName;
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
//----------------Task Dedicated Hours----------------
    public int getTaskDedicatedHours() {
        return taskDedicatedHours;
    }

    public void setTaskDedicatedHours(int taskDedicatedHours) {
        this.taskDedicatedHours = taskDedicatedHours;
    }
//----------------Task Status----------------
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
//----------------Assigned User ID For task----------------
    public int getAssignedTaskUserID() {
        return assignedTaskUserID;
    }

    public void setAssignedUserID(int assignedUserID) {
        this.assignedTaskUserID= assignedUserID;
    }
//----------------Assigned User Name For task----------------
    public String getAssignedTaskUserName() {
        return assignedTaskUserName;
    }

    public void setAssignedTaskUserName(String assignedTaskUserName) {
        this.assignedTaskUserName = assignedTaskUserName;
    }
}
