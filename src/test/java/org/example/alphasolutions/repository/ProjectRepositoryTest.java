package org.example.alphasolutions.repository;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.Interfaces.ProjectRepository;
import org.example.alphasolutions.model.Project;
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
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findAll_ShouldReturn4Projects() {
        List<Project> projects = projectRepository.findAll();
        assertEquals(4, projects.size());
    }

    @Test
    void findByID_ShouldReturnCorrectProject() {
        Project project = projectRepository.findByID(1);
        assertNotNull(project);
        assertEquals("Website Redesign", project.getProjectName());
        assertEquals("Complete redesign of the company website.", project.getProjectDescription());
        assertEquals("IN_PROGRESS", project.getProjectStatus());
        assertEquals("HIGH", project.getProjectPriority());
    }

    @Test
    void findByStatus_ShouldReturnProjectsWithMatchingStatus() {
        List<Project> inProgressProjects = projectRepository.findByStatus("IN_PROGRESS");
        assertEquals(1, inProgressProjects.size());
        assertEquals("Website Redesign", inProgressProjects.get(0).getProjectName());

        List<Project> notStartedProjects = projectRepository.findByStatus("NOT_STARTED");
        assertEquals(2, notStartedProjects.size());

        List<Project> completedProjects = projectRepository.findByStatus("COMPLETED");
        assertEquals(1, completedProjects.size());
        assertEquals("Internal CRM Update", completedProjects.get(0).getProjectName());
    }

    @Test
    void save_ShouldCreateNewProject() {
        Project newProject = new Project();
        newProject.setProjectName("New Test Project");
        newProject.setProjectDescription("Test project description");
        newProject.setProjectStartDate(LocalDate.of(2024, 6, 1));
        newProject.setProjectDeadline(LocalDate.of(2024, 12, 31));
        newProject.setProjectTimeEstimate(150);
        newProject.setProjectTimeSpent(0);
        newProject.setProjectStatus("NOT_STARTED");
        newProject.setProjectPriority("MEDIUM");

        projectRepository.save(newProject);

        List<Project> allProjects = projectRepository.findAll();
        assertEquals(5, allProjects.size());

        boolean found = false;
        for (Project p : allProjects) {
            if ("New Test Project".equals(p.getProjectName())) {
                found = true;
                assertEquals("Test project description", p.getProjectDescription());
                assertEquals("NOT_STARTED", p.getProjectStatus());
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void update_ShouldModifyProject() {
        Project project = projectRepository.findByID(2);
        project.setProjectStatus("IN_PROGRESS");
        project.setProjectTimeSpent(75);
        
        projectRepository.update(project);
        
        Project updatedProject = projectRepository.findByID(2);
        assertEquals("IN_PROGRESS", updatedProject.getProjectStatus());
        assertEquals(75, updatedProject.getProjectTimeSpent());
    }

    @Test
    void delete_ShouldRemoveProject() {
        projectRepository.delete(4);
        
        try {
            Project deletedProject = projectRepository.findByID(4);
            assertNull(deletedProject);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Incorrect result size"));
        }
        
        List<Project> remainingProjects = projectRepository.findAll();
        assertEquals(3, remainingProjects.size());
    }
} 