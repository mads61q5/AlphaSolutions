package org.example.alphasolutions.Interfaces;
import java.util.List;

import org.example.alphasolutions.model.SubProject;

public interface SubProjectRepository {
    List<SubProject> findAll();
    SubProject findByID(int subProjectID);
    int save(SubProject subProject);
    void update(SubProject subProject, int subProjectID);
    void delete(int subProjectID);
    List<SubProject> findByProjectID(int projectID);
    List<SubProject> findByStatus(String status);
    List<SubProject> findByPriority(String Priority);
}
