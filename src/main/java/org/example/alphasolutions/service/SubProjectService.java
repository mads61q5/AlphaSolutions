package org.example.alphasolutions.service;

import java.util.List;

import org.example.alphasolutions.Interfaces.SubProjectRepository;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.springframework.stereotype.Service;

@Service
    public class SubProjectService {
    private final SubProjectRepository subProjectRepository;
    private final TimeCalculationService timeCalculationService;

    public SubProjectService(SubProjectRepository subProjectRepository, TimeCalculationService timeCalculationService) {
        this.subProjectRepository = subProjectRepository;
        this.timeCalculationService = timeCalculationService;
    }
    //--------Create - CRUD OPS----------
    public SubProject createSubProject(SubProject subProject) {
       subProject.setSubProjectStatus("NOT_STARTED");
       subProject.setSubProjectTimeSpent(0);
       subProject.setSubProjectPriority("LOW_PRIORITY");
       int newId = subProjectRepository.save(subProject);
       return getSubProjectByID(newId);
    }
    //--------get all sub projects ----------
    public List<SubProject> getAllSubProjects() {
        return subProjectRepository.findAll();
    }
    //--------get sub project by id ----------
    public SubProject getSubProjectByID(int subProjectID) {
        return subProjectRepository.findByID(subProjectID);
    }
    //--------get sub projects by project id ----------
    public List<SubProject> getSubProjectsByProject(int projectID) {
        return subProjectRepository.findByProjectID(projectID);
    }
    //---------update sub project ----------
    public void updateSubProject(SubProject subProject, int subProjectID) {
        subProjectRepository.update(subProject, subProjectID);
    }
    //---------delete sub project ----------
    public void deleteSubProject(int subProjectID) {
        subProjectRepository.delete(subProjectID);
    }
    
    //---------calculate time from tasks and update----------
    public void calculateAndUpdateTimeEstimateFromTasks(SubProject subProject, List<Task> tasks) {
        int timeEstimate = timeCalculationService.calculateSubProjectTimeEstimateFromTasks(tasks);
        subProject.setSubProjectTimeEstimate(timeEstimate);
        updateSubProject(subProject, subProject.getSubProjectID());
    }
    
    //---------update subproject time when task changes----------
    public void updateTimeWhenTaskChanges(SubProject subProject, List<Task> tasks) {
        calculateAndUpdateTimeEstimateFromTasks(subProject, tasks);
    }
}
