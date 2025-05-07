package org.example.alphasolutions.Interfaces;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;

import java.util.List;

public interface SubProjectRepository {
    List<SubProject> findAll();
    SubProject findById(int subProjectID);
    void save(SubProject subProject);
    void update(SubProject subProject);
    void delete(int subProjectID);
    List<SubProject> findByProjectId(int projectID);
    List<SubProject> findByStatus(String status);
    List<SubProject> findByPriority(String Priority);
}
