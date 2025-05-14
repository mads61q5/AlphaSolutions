
package org.example.alphasolutions.Interfaces;
import org.example.alphasolutions.model.Task;
import java.util.List;


public interface TaskRepository {
    List<Task> findAll();
    Task findByID(int taskID);
    void save(Task task);
    void update(Task task, int taskID);
    void delete(int taskID);
    //List<Task> findByProjectID(int projectID);
    List<Task> findBySubProjectID(int subProjectID);
    //List<Task> findBySubProjectIDAndStatus(int subProjectID, String status);
    List<Task> findTaskByStatus(int subProjectID, String status);
    List<Task> findTaskByPriority(int subProjectID, String Priority);
}

