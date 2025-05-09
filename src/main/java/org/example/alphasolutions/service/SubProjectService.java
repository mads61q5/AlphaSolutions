package org.example.alphasolutions.service;

import org.example.alphasolutions.Interfaces.SubProjectRepository;
import org.example.alphasolutions.model.SubProject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class SubProjectService {
    private final SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }
    //--------Create - CRUD OPS----------
    public void createSubProject(SubProject subProject) {
       subProject.setSubProjectStatus("NOT_STARTED");
       subProject.setSubProjectTimeSpent(0);
       subProject.setSubProjectPriority("LOW_PRIORITY");
        subProjectRepository.save(subProject);

    }
    //--------get all sub projects ----------
    public List<SubProject> getAllSubProjects() {
        return subProjectRepository.findAll();
    }
    //--------get sub project by id ----------
    public SubProject getSubProjectById(int subProjectID) {
        return subProjectRepository.findById(subProjectID);
    }
    //--------get sub projects by project id ----------
    public List<SubProject> getSubProjectsByProject(int projectID) {
        return subProjectRepository.findByProjectId(projectID);
    }
    //---------update sub project ----------
    public void updateSubProject(SubProject subProject, int subProjectID) {
        subProjectRepository.update(subProject, subProjectID);
    }
    //---------delete sub project ----------
    public void deleteSubProject(int subProjectID) {
        subProjectRepository.delete(subProjectID);
    }
}
