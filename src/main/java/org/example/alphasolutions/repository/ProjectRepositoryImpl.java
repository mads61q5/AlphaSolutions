package org.example.alphasolutions.repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Project> findAll() {
        String sql = "SELECT * FROM projects";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    @Override
    public Project findById(int projectID) {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        return jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), projectID);
    }

    @Override
    public List<Project> findByStatus(String status) {
        String sql = "SELECT * FROM projects WHERE project_status = ?";
        return jdbcTemplate.query(sql, new ProjectRowMapper(), status);
    }

    @Override
    public void save(Project project) {
        String sql = "INSERT INTO projects (project_name, project_description, project_start_date, " +
                "project_deadline, project_time_estimate, project_time_spent, project_status, " +
                "project_priority) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getProjectName());
            ps.setString(2, project.getProjectDescription());
            ps.setDate(3, project.getProjectStartDate() != null ? java.sql.Date.valueOf(project.getProjectStartDate()) : null);
            ps.setDate(4, project.getProjectDeadline() != null ? java.sql.Date.valueOf(project.getProjectDeadline()) : null);
            ps.setInt(5, project.getProjectTimeEstimate());
            ps.setInt(6, project.getProjectTimeSpent());
            ps.setString(7, project.getProjectStatus());
            ps.setString(8, project.getProjectPriority());
            return ps;
        }, keyHolder);

        // Retrieve and set the generated ID
        if (keyHolder.getKey() != null) {
            project.setProjectID(keyHolder.getKey().intValue());
        }
    }

    @Override
    public void update(Project project) {
        String sql = "UPDATE projects SET project_name = ?, project_description = ?, " +
                "project_start_date = ?, project_deadline = ?, project_time_estimate = ?, " +
                "project_time_spent = ?, project_status = ?, project_priority = ? " +
                "WHERE project_id = ?";

        jdbcTemplate.update(sql,
                project.getProjectName(),
                project.getProjectDescription(),
                project.getProjectStartDate(),
                project.getProjectDeadline(),
                project.getProjectTimeEstimate(),
                project.getProjectTimeSpent(),
                project.getProjectStatus(),
                project.getProjectPriority(),
                project.getProjectID());
    }

    @Override
    public void delete(int projectID) {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        jdbcTemplate.update(sql, projectID);
    }

    private static class ProjectRowMapper implements RowMapper<Project> {
        @Override
        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Project(
                    rs.getInt("project_id"),
                    rs.getString("project_name"),
                    rs.getString("project_description"),
                    rs.getDate("project_start_date").toLocalDate(),
                    rs.getDate("project_deadline").toLocalDate(),
                    rs.getInt("project_time_estimate"),
                    rs.getInt("project_time_spent"),
                    rs.getString("project_status"),
                    rs.getString("project_priority")
            );
        }
    }
}
