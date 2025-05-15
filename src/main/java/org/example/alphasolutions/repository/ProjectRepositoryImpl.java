package org.example.alphasolutions.repository;

import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    public Project findByID(int projectID) {
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
                             "project_deadline, project_time_estimate, project_time_spent, project_status, project_priority) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
                            project.getProjectName(),
                            project.getProjectDescription(),
                            project.getProjectStartDate(),
                            project.getProjectDeadline(),
                            project.getProjectTimeEstimate(),
                            project.getProjectTimeSpent(),
                            project.getProjectStatus(),
                            project.getProjectPriority());
    }

    @Override
    public void update(Project project) {
        String sql =  "UPDATE projects SET project_name = ?, project_description = ?, " +
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
        String deleteSubprojectsSql = "DELETE FROM subprojects WHERE project_id = ?";
        jdbcTemplate.update(deleteSubprojectsSql, projectID);

        
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

    // --------------get project with its subprojects
    public Project findByIdWithSubprojects(int projectID) {
        String sql = "SELECT p.* FROM projects p WHERE p.project_id = ?";
        Project project = jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), projectID);

        if (project != null) {
            String subprojectsSql = "SELECT * FROM subprojects WHERE project_id = ?";
            List<SubProject> subprojects = jdbcTemplate.query(subprojectsSql,
                    new SubProjectRowMapper(), projectID);
            project.setSubProject(subprojects);
        }

        return project;
    }

    private static class SubProjectRowMapper implements RowMapper<SubProject> {
        @Override
        public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SubProject(
                    rs.getInt("subproject_id"),
                    rs.getString("subproject_name"),
                    rs.getString("subproject_description"),
                    rs.getDate("subproject_start_date").toLocalDate(),
                    rs.getDate("subproject_deadline").toLocalDate(),
                    rs.getInt("subproject_time_estimate"),
                    rs.getInt("subproject_time_spent"),
                    rs.getString("subproject_status"),
                    rs.getString("subproject_priority"),
                    rs.getInt("project_id")
            );
        }
    }
}