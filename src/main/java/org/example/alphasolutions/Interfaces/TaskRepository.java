package org.example.alphasolutions.Interfaces;
import java.util.List;

import org.example.alphasolutions.model.Task;


public interface TaskRepository {
    List<Task> findAll();
    Task findByID(int taskID);
    void save(Task task);
    void update(Task task, int taskID);
    void delete(int taskID);
    //List<Task> findByProjectID(int projectID);
    List<Task> findBySubProjectID(int subProjectID);
    List<Task> findTasksBySubProjectIDAndStatus(int subProjectID, String status);
    List<Task> findByUserID(int userID);
    //List<Task> findBySubProjectIDAndStatus(int subProjectID, String status);
    //List<Task> findTaskByStatus(String status);
    //List<Task> findTaskByPriority(String Priority);
}

