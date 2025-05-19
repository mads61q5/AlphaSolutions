package org.example.alphasolutions.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.example.alphasolutions.Interfaces.TaskRepository;
import org.example.alphasolutions.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    @Override
    public Task findByID(int taskID) {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), taskID);
    }

    @Override
    public List<Task> findBySubProjectID(int subProjectID) {
        String sql = "SELECT * FROM tasks WHERE subproject_id = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), subProjectID);
    }

    @Override
    public List<Task> findTasksBySubProjectIDAndStatus(int subProjectID, String status) {
        String sql = "SELECT * FROM tasks WHERE subproject_id = ? AND task_status = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), subProjectID, status);
    }

    @Override
    public void save(Task task) {
        String sql = "INSERT INTO tasks (task_name, task_description, task_start_date, " +
                "task_deadline, task_time_estimate, task_time_spent, task_status, " +
                "task_priority, subproject_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStartDate(),
                task.getTaskDeadline(),
                task.getTaskTimeEstimate(),
                task.getTaskTimeSpent(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getSubProjectID(),
                task.getUserID() == 0 ? null : task.getUserID());
    }

    @Override
    public void update(Task task, int taskID) {
        String sql = "UPDATE tasks SET task_name = ?, task_description = ?, " +
                "task_start_date = ?, task_deadline = ?, task_time_estimate = ?, " +
                "task_time_spent = ?, task_status = ?, task_priority = ?, " +
                "subproject_id = ?, user_id = ? WHERE task_id = ?";

        jdbcTemplate.update(sql,
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStartDate(),
                task.getTaskDeadline(),
                task.getTaskTimeEstimate(),
                task.getTaskTimeSpent(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getSubProjectID(),
                task.getUserID() == 0 ? null : task.getUserID(),
                taskID);
    }

    @Override
    public void delete(int taskID) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        jdbcTemplate.update(sql, taskID);
    }

    @Override
    public List<Task> findByUserID(int userID) {
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), userID);
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            java.sql.Date sqlStartDate = rs.getDate("task_start_date");
            java.sql.Date sqlDeadline = rs.getDate("task_deadline");

            return new Task(
                    rs.getInt("task_id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    sqlStartDate != null ? sqlStartDate.toLocalDate() : null,
                    sqlDeadline != null ? sqlDeadline.toLocalDate() : null,
                    rs.getInt("task_time_estimate"),
                    rs.getInt("task_time_spent"),
                    rs.getString("task_status"),
                    rs.getString("task_priority"),
                    rs.getInt("subproject_id"),
                    rs.getInt("user_id")
            );
        }
    }
}