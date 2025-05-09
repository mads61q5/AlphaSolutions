package org.example.alphasolutions.service;

import org.example.alphasolutions.Interfaces.TaskRepository;
import org.example.alphasolutions.model.Task;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public void createTask(Task task) {
        task.setTaskStatus("NOT_STARTED");
        task.setTaskTimeSpent(0);
        task.setTaskPriority("LOW_PRIORITY");
        taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(int taskID) {
        return taskRepository.findById(taskID);
    }
    public List<Task> getTasksByProject(int projectID) {
        return taskRepository.findByProjectId(projectID);
    }
    public List<Task> getTasksBySubProjectId(int subProjectID) {
        return taskRepository.findBySubProjectId(subProjectID);
    }
    public void updateTask(Task task, int taskID) {
        taskRepository.update(task, taskID);
    }

    public void deleteTask(int taskID) {
        taskRepository.delete(taskID);
    }

}
