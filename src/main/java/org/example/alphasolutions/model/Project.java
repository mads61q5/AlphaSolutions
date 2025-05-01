package org.example.alphasolutions.model;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
//------Attributes----------
public class Project {
    private int projectID;
    private String projectName;
    private String projectDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectDeadline;
    private int projectTimeEstimate; // in hours
    private int projectDedicatedHours; // hours spent so far
    private String projectStatus;
    private int projectManagerID;
    private String projectManagerName;


    public Project() {
    }
//----------------Constructor----------------
    public Project(int projectID, String projectName, String projectDescription,
                   LocalDate projectStartDate, LocalDate projectDeadline,
                   int projectTimeEstimate, int projectDedicatedHours,
                   String projectStatus, int projectManagerID, String projectManagerName) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStartDate = projectStartDate;
        this.projectDeadline = projectDeadline;
        this.projectTimeEstimate = projectTimeEstimate;
        this.projectDedicatedHours = projectDedicatedHours;
        this.projectStatus = projectStatus;
        this.projectManagerID = projectManagerID;
        this.projectManagerName = projectManagerName;
    }
    //------Setters & getters----------
//----------------Project ID----------------
    // Getters and Setters
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
//----------------Project name-----------------
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
//----------------Project description----------------
    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
//----------------Project start date----------------
    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }
//----------------Project deadline----------------
    public LocalDate getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(LocalDate projectDeadline) {
        this.projectDeadline = projectDeadline;
    }
//----------------Project time estimate----------------
    public int getProjectTimeEstimate() {
        return projectTimeEstimate;
    }

    public void setProjectTimeEstimate(int projectTimeEstimate) {
        this.projectTimeEstimate = projectTimeEstimate;
    }
//----------------Project dedicated hours----------------
    public int getProjectDedicatedHours() {
        return projectDedicatedHours;
    }

    public void setProjectDedicatedHours(int projectDedicatedHours) {
        this.projectDedicatedHours = projectDedicatedHours;
    }
//----------------Project status----------------
    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
//----------------Project manager ID----------------
    public int getProjectManagerID() {
        return projectManagerID;
    }

    public void setProjectManagerID(int projectManagerID) {
        this.projectManagerID = projectManagerID;
    }
//----------------Project manager name----------------
    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;

    }
}