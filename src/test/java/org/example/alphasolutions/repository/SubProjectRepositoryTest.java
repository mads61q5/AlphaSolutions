package org.example.alphasolutions.repository;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.Interfaces.SubProjectRepository;
import org.example.alphasolutions.model.SubProject;
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
class SubProjectRepositoryTest {

    @Autowired
    private SubProjectRepository subProjectRepository;

    @Test
    void findAll_ShouldReturn5SubProjects() {
        List<SubProject> subProjects = subProjectRepository.findAll();
        assertEquals(5, subProjects.size());
    }

    @Test
    void findByID_ShouldReturnCorrectSubProject() {
        SubProject subProject = subProjectRepository.findByID(1);
        assertNotNull(subProject);
        assertEquals("Phase 1: Design Mockups", subProject.getSubProjectName());
        assertEquals("Create and approve design mockups.", subProject.getSubProjectDescription());
        assertEquals("COMPLETED", subProject.getSubProjectStatus());
        assertEquals("HIGH", subProject.getSubProjectPriority());
        assertEquals(1, subProject.getProjectID());
    }

    @Test
    void findByProjectID_ShouldReturnSubProjectsForProject() {
        List<SubProject> project1SubProjects = subProjectRepository.findByProjectID(1);
        assertEquals(3, project1SubProjects.size());
        
        List<SubProject> project2SubProjects = subProjectRepository.findByProjectID(2);
        assertEquals(2, project2SubProjects.size());
    }

    @Test
    void findByStatus_ShouldReturnSubProjectsWithMatchingStatus() {
        List<SubProject> completedSubProjects = subProjectRepository.findByStatus("COMPLETED");
        assertEquals(1, completedSubProjects.size());
        assertEquals("Phase 1: Design Mockups", completedSubProjects.get(0).getSubProjectName());

        List<SubProject> inProgressSubProjects = subProjectRepository.findByStatus("IN_PROGRESS");
        assertEquals(2, inProgressSubProjects.size());

        List<SubProject> notStartedSubProjects = subProjectRepository.findByStatus("NOT_STARTED");
        assertEquals(2, notStartedSubProjects.size());
    }

    @Test
    void findByPriority_ShouldReturnSubProjectsWithMatchingPriority() {
        List<SubProject> highPrioritySubProjects = subProjectRepository.findByPriority("HIGH");
        assertEquals(4, highPrioritySubProjects.size());

        List<SubProject> mediumPrioritySubProjects = subProjectRepository.findByPriority("MEDIUM");
        assertEquals(1, mediumPrioritySubProjects.size());
        assertEquals("Phase 3: Backend Integration", mediumPrioritySubProjects.get(0).getSubProjectName());
    }

    @Test
    void save_ShouldCreateNewSubProjectWithReturnedID() {
        SubProject newSubProject = new SubProject();
        newSubProject.setSubProjectName("New Test SubProject");
        newSubProject.setSubProjectDescription("Test subproject description");
        newSubProject.setSubProjectStartDate(LocalDate.of(2024, 6, 1));
        newSubProject.setSubProjectDeadline(LocalDate.of(2024, 8, 31));
        newSubProject.setSubProjectTimeEstimate(50);
        newSubProject.setSubProjectTimeSpent(0);
        newSubProject.setSubProjectStatus("NOT_STARTED");
        newSubProject.setSubProjectPriority("LOW");
        newSubProject.setProjectID(1);

        int generatedId = subProjectRepository.save(newSubProject);
        assertTrue(generatedId > 0);

        SubProject savedSubProject = subProjectRepository.findByID(generatedId);
        assertNotNull(savedSubProject);
        assertEquals("New Test SubProject", savedSubProject.getSubProjectName());
        assertEquals("Test subproject description", savedSubProject.getSubProjectDescription());
        assertEquals(1, savedSubProject.getProjectID());

        List<SubProject> allSubProjects = subProjectRepository.findAll();
        assertEquals(6, allSubProjects.size());
    }

    @Test
    void update_ShouldModifySubProject() {
        SubProject subProject = subProjectRepository.findByID(2);
        subProject.setSubProjectStatus("COMPLETED");
        subProject.setSubProjectTimeSpent(120);
        
        subProjectRepository.update(subProject, 2);
        
        SubProject updatedSubProject = subProjectRepository.findByID(2);
        assertEquals("COMPLETED", updatedSubProject.getSubProjectStatus());
        assertEquals(120, updatedSubProject.getSubProjectTimeSpent());
    }

    @Test
    void delete_ShouldRemoveSubProject() {
        subProjectRepository.delete(5);
        
        try {
            SubProject deletedSubProject = subProjectRepository.findByID(5);
            assertNull(deletedSubProject);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Incorrect result size"));
        }
        
        List<SubProject> remainingSubProjects = subProjectRepository.findAll();
        assertEquals(4, remainingSubProjects.size());
    }
} 