package org.example.alphasolutions.service;

import org.example.alphasolutions.Interfaces.TaskRepository;
import org.example.alphasolutions.model.Task;
import org.springframework.stereotype.Service;

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
    
    public Task getTaskByID(int taskID) {
        return taskRepository.findByID(taskID);
    }
    
    public List<Task> getTasksBySubProjectID(int subProjectID) {
        return taskRepository.findBySubProjectID(subProjectID);
    }
    
    public void updateTask(Task task, int taskID) {
        task.setTaskID(taskID);
        taskRepository.update(task, taskID);
    }
    
    public void deleteTask(int taskID) {
        taskRepository.delete(taskID);
    }
    
    public List<Task> getTasksByStatus(int subProjectID, String taskStatus) {
        return taskRepository.findTaskByStatus(subProjectID,taskStatus);
    }
    public List<Task> getTasksByPriority(int subProjectID, String taskPriority) {
        return taskRepository.findTaskByPriority(subProjectID,taskPriority);
    }
}