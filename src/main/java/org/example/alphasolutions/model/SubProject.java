

package org.example.alphasolutions.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubProject {
    private int subProjectID;
    private String subProjectName;
    private String subProjectDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate subProjectStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate subProjectDeadline;
    private int subProjectTimeEstimate;
    private int subProjectTimeSpent;
    private String subProjectStatus;
    private String subProjectPriority;
    private final List<Task> tasks = new ArrayList<>();
    private int projectID;
   
    
    public SubProject() {
    }
    public SubProject(int subProjectID, String subProjectName, String subProjectDescription,
                   LocalDate subProjectStartDate, LocalDate subProjectDeadline,
                   int subProjectTimeEstimate, int subProjectTimeSpent,
                   String subProjectStatus, String subProjectPriority,int projectID) {
        this.subProjectID = subProjectID;
        this.subProjectName = subProjectName;
        this.subProjectDescription = subProjectDescription;
        this.subProjectStartDate = subProjectStartDate;
        this.subProjectDeadline = subProjectDeadline;
        this.subProjectTimeEstimate = subProjectTimeEstimate;
        this.subProjectTimeSpent = subProjectTimeSpent;
        this.subProjectStatus = subProjectStatus;
        this.subProjectPriority = subProjectPriority;
        this.projectID = projectID;
        
    }
    
    public List<Task> getTasks() {
        return tasks;
        
    }
    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }
    
    /*public void addTask(Task task) {
        tasks.add(task);
    }
    
    public void removeTask(Task task) {
        tasks.remove(task);
    }
    
     */
    
    // Getters and Setters
    public int getSubProjectID() {
        return subProjectID;
    }
    
    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }
    
    public String getSubProjectName() {
        return subProjectName;
    }
    
    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }
    
    public String getSubProjectDescription() {
        return subProjectDescription;
    }
    
    public void setSubProjectDescription(String subProjectDescription) {
        this.subProjectDescription = subProjectDescription;
    }
    
    public LocalDate getSubProjectStartDate() {
        return subProjectStartDate;
    }
    
    public void setSubProjectStartDate(LocalDate subProjectStartDate) {
        this.subProjectStartDate = subProjectStartDate;
    }
    
    public LocalDate getSubProjectDeadline() {
        return subProjectDeadline;
    }
    
    public void setSubProjectDeadline(LocalDate subProjectDeadline) {
        this.subProjectDeadline = subProjectDeadline;
    }
    
    public int getSubProjectTimeEstimate() {
        return subProjectTimeEstimate;
    }
    
    public void setSubProjectTimeEstimate(int subProjectTimeEstimate) {
        this.subProjectTimeEstimate = subProjectTimeEstimate;
    }
    
    public int getSubProjectTimeSpent() {
        return subProjectTimeSpent;
    }
    
    public void setSubProjectTimeSpent(int subProjectTimeSpent) {
        this.subProjectTimeSpent = subProjectTimeSpent;
    }
    
    public String getSubProjectStatus() {
        return subProjectStatus;
    }
    
    public void setSubProjectStatus(String subProjectStatus) {
        this.subProjectStatus = subProjectStatus;
    }
    
    public String getSubProjectPriority() {
        return subProjectPriority;
    }
    
    public void setSubProjectPriority(String subProjectPriority) {
        this.subProjectPriority = subProjectPriority;
    }
    public int getProjectID() {
        return projectID;
    }
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}