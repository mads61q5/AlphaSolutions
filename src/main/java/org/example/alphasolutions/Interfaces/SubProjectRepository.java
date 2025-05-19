package org.example.alphasolutions.Interfaces;
import org.example.alphasolutions.model.SubProject;

import java.util.List;

public interface SubProjectRepository {
    List<SubProject> findAll();
    SubProject findByID(int subProjectID);
    void save(SubProject subProject);
    void update(SubProject subProject, int subProjectID);
    void delete(int subProjectID);
    List<SubProject> findByProjectID(int projectID);
    List<SubProject> findByStatus(String status);
    List<SubProject> findByPriority(String Priority);
}
