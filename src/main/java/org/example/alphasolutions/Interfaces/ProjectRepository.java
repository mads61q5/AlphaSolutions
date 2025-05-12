package org.example.alphasolutions.Interfaces;
import org.example.alphasolutions.model.Project;
import java.util.List;


public interface ProjectRepository {
    List<Project> findAll();
    Project findByID(int projectID);
    void save(Project project);
    void update(Project project);
    void delete(int projectID);
    List<Project> findByStatus(String status);
}
