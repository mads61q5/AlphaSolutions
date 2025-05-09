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
    //----------create task----------
    public void createTask(Task task) {
        task.setTaskStatus("NOT_STARTED");
        task.setTaskTimeSpent(0);
        task.setTaskPriority("LOW_PRIORITY");
        taskRepository.save(task);
    }
//--------Get All Tasks-----------
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
//--------Get Task By Id-----------
    public Task getTaskById(int taskID) {
        return taskRepository.findById(taskID);
    }
    //--------Get Tasks By Project Id-----------
    public List<Task> getTasksByProject(int projectID) {
        return taskRepository.findByProjectId(projectID);
    }
    //--------Get Tasks By Sub Project Id-----------
    public List<Task> getTasksBySubProjectId(int subProjectID) {
        return taskRepository.findBySubProjectId(subProjectID);
    }
    //--------Update Task-----------
    public void updateTask(Task task, int taskID) {
        taskRepository.update(task,taskID);
    }
    //--------Delete Task-----------
    public void deleteTask(int taskID) {
        taskRepository.delete(taskID);
    }

}
