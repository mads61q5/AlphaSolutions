package org.example.alphasolutions.model;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//------Attributes----------
public class Project {
    private int projectID;
    private String projectName;
    private String projectDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectDeadline;
    private int projectTimeEstimate;
    private int projectTimeSpent;
    private String projectStatus;
    private String projectPriority;
    private List<Task> Tasks = new ArrayList<>();
    private List<SubProject>subProjects = new ArrayList<>();


    public Project() {
    }
//----------------Constructor----------------
public Project(int projectID, String projectName, String projectDescription, LocalDate projectStartDate, LocalDate projectDeadline,
               int projectTimeEstimate, int projectTimeSpent, String projectStatus, String projectPriority) {
    this.projectID = projectID;
    this.projectName = projectName;
    this.projectDescription = projectDescription;
    this.projectStartDate = projectStartDate;
    this.projectDeadline = projectDeadline;
    this.projectTimeEstimate = projectTimeEstimate;
    this.projectTimeSpent = projectTimeSpent;
    this.projectStatus = projectStatus;
    this.projectPriority = projectPriority;
    }
    //------Setters & getters----------
    public List<Task> getTasks() {
        return Tasks;

    }
    public List<SubProject> getSubProjects() {
        return subProjects;
    }
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
//----------------Project time spent----------------
public int getProjectTimeSpent() {
    return projectTimeSpent;
    }

    public void setProjectTimeSpent(int projectTimeSpent) {
        this.projectTimeSpent = projectTimeSpent;
    }
//----------------Project status----------------
    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
//----------------Project priority---------------
public String getProjectPriority() {
    return projectPriority;
}
    public void setProjectPriority(String projectPriority) {
        this.projectPriority = projectPriority;
    }
}