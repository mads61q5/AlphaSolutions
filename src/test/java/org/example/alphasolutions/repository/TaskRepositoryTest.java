package org.example.alphasolutions.repository;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.Interfaces.TaskRepository;
import org.example.alphasolutions.model.Task;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:h2init.sql")
@Transactional
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void findAll_ShouldReturn6Tasks() {
        List<Task> tasks = taskRepository.findAll();
        assertEquals(6, tasks.size());
    }

    @Test
    void findByID_ShouldReturnCorrectTask() {
        Task task = taskRepository.findByID(1);
        assertNotNull(task);
        assertEquals("Homepage Layout", task.getTaskName());
        assertEquals("Implement HTML/CSS for homepage.", task.getTaskDescription());
        assertEquals("COMPLETED", task.getTaskStatus());
        assertEquals("HIGH", task.getTaskPriority());
        assertEquals(2, task.getSubProjectID());
    }

    @Test
    void findBySubProjectID_ShouldReturnTasksForSubProject() {
        List<Task> subProject2Tasks = taskRepository.findBySubProjectID(2);
        assertEquals(4, subProject2Tasks.size());
        
        List<Task> subProject4Tasks = taskRepository.findBySubProjectID(4);
        assertEquals(2, subProject4Tasks.size());
    }

    @Test
    void findTasksBySubProjectIDAndStatus_ShouldReturnFilteredTasks() {
        List<Task> inProgressTasks = taskRepository.findTasksBySubProjectIDAndStatus(2, "IN_PROGRESS");
        assertEquals(3, inProgressTasks.size());

        List<Task> completedTasks = taskRepository.findTasksBySubProjectIDAndStatus(2, "COMPLETED");
        assertEquals(1, completedTasks.size());
        assertEquals("Homepage Layout", completedTasks.get(0).getTaskName());

        List<Task> notStartedTasks = taskRepository.findTasksBySubProjectIDAndStatus(4, "NOT_STARTED");
        assertEquals(2, notStartedTasks.size());
    }

    @Test
    void findByUserID_ShouldReturnUserAssignedTasks() {
        List<Task> nullUserTasks = taskRepository.findByUserID(0);
        assertEquals(0, nullUserTasks.size());
    }

    @Test
    void save_ShouldCreateNewTaskWithNullableUserID() {
        Task newTask = new Task();
        newTask.setTaskName("New Test Task");
        newTask.setTaskDescription("Test task description");
        newTask.setTaskStartDate(LocalDate.of(2024, 6, 1));
        newTask.setTaskDeadline(LocalDate.of(2024, 6, 15));
        newTask.setTaskTimeEstimate(25);
        newTask.setTaskTimeSpent(0);
        newTask.setTaskStatus("NOT_STARTED");
        newTask.setTaskPriority("LOW");
        newTask.setSubProjectID(2);
        newTask.setUserID(0);

        taskRepository.save(newTask);

        List<Task> allTasks = taskRepository.findAll();
        assertEquals(7, allTasks.size());

        boolean found = false;
        for (Task t : allTasks) {
            if ("New Test Task".equals(t.getTaskName())) {
                found = true;
                assertEquals("Test task description", t.getTaskDescription());
                assertEquals("NOT_STARTED", t.getTaskStatus());
                assertEquals(2, t.getSubProjectID());
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void save_ShouldCreateTaskWithAssignedUser() {
        Task newTask = new Task();
        newTask.setTaskName("Assigned Task");
        newTask.setTaskDescription("Task assigned to user");
        newTask.setTaskStartDate(LocalDate.of(2024, 6, 1));
        newTask.setTaskDeadline(LocalDate.of(2024, 6, 15));
        newTask.setTaskTimeEstimate(40);
        newTask.setTaskTimeSpent(0);
        newTask.setTaskStatus("NOT_STARTED");
        newTask.setTaskPriority("MEDIUM");
        newTask.setSubProjectID(2);
        newTask.setUserID(1);

        taskRepository.save(newTask);

        List<Task> user1Tasks = taskRepository.findByUserID(1);
        assertEquals(1, user1Tasks.size());
        assertEquals("Assigned Task", user1Tasks.get(0).getTaskName());
    }

    @Test
    void update_ShouldModifyTaskFields() {
        Task task = taskRepository.findByID(2);
        task.setTaskStatus("COMPLETED");
        task.setTaskTimeSpent(40);
        task.setUserID(2);
        
        taskRepository.update(task, 2);
        
        Task updatedTask = taskRepository.findByID(2);
        assertEquals("COMPLETED", updatedTask.getTaskStatus());
        assertEquals(40, updatedTask.getTaskTimeSpent());
        assertEquals(2, updatedTask.getUserID());
    }

    @Test
    void delete_ShouldRemoveTask() {
        taskRepository.delete(6);
        
        try {
            Task deletedTask = taskRepository.findByID(6);
            assertNull(deletedTask);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Incorrect result size"));
        }
        
        List<Task> remainingTasks = taskRepository.findAll();
        assertEquals(5, remainingTasks.size());
    }
} 