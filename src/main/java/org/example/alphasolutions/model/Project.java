package org.example.alphasolutions.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private final List<SubProject> subProjects = new ArrayList<>();

    public Project() {
    }

    public Project(int projectID, String projectName, String projectDescription,
                   LocalDate projectStartDate, LocalDate projectDeadline,
                   int projectTimeEstimate, int projectTimeSpent,
                   String projectStatus, String projectPriority) {
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
    
    
    public List<SubProject> getSubProjects() {
        return subProjects;
    }

    public void addSubProject(SubProject subProject) {
        if (subProject == null) {
            throw new IllegalArgumentException("SubProject cannot be null");
        }
        if (subProject.getProjectID() != this.projectID) {
            throw new IllegalArgumentException("SubProject does not belong to this project");
        }
        subProjects.add(subProject);
    }
    public void setSubProject(List<SubProject> subProjects) {
        if (subProjects == null) {
            throw new IllegalArgumentException(" ");
        }
        this.subProjects.clear();
        this.subProjects.addAll(subProjects);
    }

  //  public void removeSubProject(SubProject subProject) {
    //    subProjects.remove(subProject);
    

    // Getters and Setters
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(LocalDate projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    public int getProjectTimeEstimate() {
        return projectTimeEstimate;
    }

    public void setProjectTimeEstimate(int projectTimeEstimate) {
        this.projectTimeEstimate = projectTimeEstimate;
    }

    public int getProjectTimeSpent() {
        return projectTimeSpent;
    }

    public void setProjectTimeSpent(int projectTimeSpent) {
        this.projectTimeSpent = projectTimeSpent;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectPriority() {
        return projectPriority;
    }

    public void setProjectPriority(String projectPriority) {
        this.projectPriority = projectPriority;
    }
    
}