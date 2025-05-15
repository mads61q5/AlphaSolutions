package org.example.alphasolutions.Interfaces;

import org.example.alphasolutions.model.SubProject;
import java.util.List;

public interface SubProjectRepository {
    // Basic CRUD operations
    List<SubProject> findAll();
    SubProject findByID(int subProjectID);
    void save(SubProject subProject);
    void update(SubProject subProject);
    void delete(int subProjectID);

    // Project relationship queries
    List<SubProject> findByProjectID(int projectID);

    // Status/Priority filters
    List<SubProject> findByStatus(String status);
    List<SubProject> findByPriority(String priority);

    // Added method to get subprojects with their tasks
    List<SubProject> findByProjectIDWithTasks(int projectID);
}