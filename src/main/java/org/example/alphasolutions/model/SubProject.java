package org.example.alphasolutions.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

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
    private int projectID;

    public SubProject() {}

    public SubProject(int subProjectID, String subProjectName, String subProjectDescription,
                      LocalDate subProjectStartDate, LocalDate subProjectDeadline,
                      int subProjectTimeEstimate, int subProjectTimeSpent,
                      String subProjectStatus, String subProjectPriority,
                      int projectID) {
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

    // Getters and Setters
    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }
// --------Sub Project name----------
    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }
//----------Project description----------
    public String getSubProjectDescription() {
        return subProjectDescription;
    }

    public void setSubProjectDescription(String subProjectDescription) {
        this.subProjectDescription = subProjectDescription;
    }
//----------Project start date----------
    public LocalDate getSubProjectStartDate() {
        return subProjectStartDate;
    }

    public void setSubProjectStartDate(LocalDate subProjectStartDate) {
        this.subProjectStartDate = subProjectStartDate;
    }
//----------Project deadline----------
    public LocalDate getSubProjectDeadline() {
        return subProjectDeadline;
    }

    public void setSubProjectDeadline(LocalDate subProjectDeadline) {
        this.subProjectDeadline = subProjectDeadline;
    }
//----------Project time estimate----------
    public int getSubProjectTimeEstimate() {
        return subProjectTimeEstimate;
    }

    public void setSubProjectTimeEstimate(int subProjectTimeEstimate) {
        this.subProjectTimeEstimate = subProjectTimeEstimate;
    }
//----------Project time spent----------
    public int getSubProjectTimeSpent() {
        return subProjectTimeSpent;
    }

    public void setSubProjectTimeSpent(int subProjectTimeSpent) {
        this.subProjectTimeSpent = subProjectTimeSpent;
    }
//----------Project status----------
    public String getSubProjectStatus() {
        return subProjectStatus;
    }

    public void setSubProjectStatus(String subProjectStatus) {
        this.subProjectStatus = subProjectStatus;
    }
//----------Project priority----------
    public String getSubProjectPriority() {
        return subProjectPriority;
    }

    public void setSubProjectPriority(String subProjectPriority) {
        this.subProjectPriority = subProjectPriority;
    }
//----------ProjectID----------
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}