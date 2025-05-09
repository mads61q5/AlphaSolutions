package org.example.alphasolutions.repository;
import org.example.alphasolutions.Interfaces.TaskRepository;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    public Task findById(int taskID) {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), taskID);
    }

    @Override
    public List<Task> findByProjectId(int projectID) {
        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), projectID);
    }
    @Override
    public List<Task> findBySubProjectId(int subProjectID) {
        String sql = "SELECT * FROM tasks WHERE subProject_id = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), subProjectID);
    }


    @Override
    public List<Task> findByProjectIdAndStatus(int projectID, String status) {
        String sql = "SELECT * FROM tasks WHERE project_id = ? AND task_status = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), projectID, status);
    }

    @Override
    public List<Task> findByStatus(String status) {
        String sql = "SELECT * FROM tasks WHERE task_status = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), status);
    }
    @Override
    public List<Task> findByPriority(String priority) {
        String sql = "SELECT * FROM subprojects WHERE tasks_priority = ?";
        return jdbcTemplate.query(sql, new TaskRowMapper(), priority);
    }

    @Override
    public void save(Task task) {
        String sql = "INSERT INTO tasks (task_name, task_description, task_start_date, " +
                "task_deadline, task_time_estimate, task_time_spent, task_status, " +
                "task_priority, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStartDate(),
                task.getTaskDeadline(),
                task.getTaskTimeEstimate(),
                task.getTaskTimeSpent(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getProjectID());
    }

    @Override
    public void update(Task task, int taskID) {
        String sql = "UPDATE tasks SET task_name = ?, task_description = ?, " +
                "task_start_date = ?, task_deadline = ?, task_time_estimate = ?, " +
                "task_time_spent = ?, task_status = ?, task_priority = ?, " +
                "project_id = ? WHERE task_id = ?";

        jdbcTemplate.update(sql,
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStartDate(),
                task.getTaskDeadline(),
                task.getTaskTimeEstimate(),
                task.getTaskTimeSpent(),
                task.getTaskStatus(),
                task.getTaskPriority(),
                task.getProjectID(),
                task.getTaskID());
    }

    @Override
    public void delete(int taskID) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        jdbcTemplate.update(sql, taskID);
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Task(
                    rs.getInt("task_id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    rs.getDate("task_start_date").toLocalDate(),
                    rs.getDate("task_deadline").toLocalDate(),
                    rs.getInt("task_time_estimate"),
                    rs.getInt("task_time_spent"),
                    rs.getString("task_status"),
                    rs.getString("task_priority"),
                    rs.getInt("project_id")
            );
        }
    }
}
