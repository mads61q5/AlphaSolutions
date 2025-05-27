package org.example.alphasolutions.service;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.TimeSummary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TimeCalculationServiceTest {
    private final TimeCalculationService timeCalcService = new TimeCalculationService();

    @Test 
    void calculateProjectTimeEstimateFromSubProjects_SumsEstimatesCorrectly() {
        // Prepare subprojects with known estimates
        SubProject s1 = new SubProject(); s1.setSubProjectTimeEstimate(30);
        SubProject s2 = new SubProject(); s2.setSubProjectTimeEstimate(70);
        List<SubProject> subs = List.of(s1, s2);
        // Execute and verify sum of estimates
        int totalEstimate = timeCalcService.calculateProjectTimeEstimateFromSubProjects(subs);
        assertEquals(100, totalEstimate);
    }

    @Test 
    void calculateProjectTimeSummary_CalculatesAggregateAndOnTrack() {
        // Prepare a Project with a future deadline and subprojects with times
        Project project = new Project();
        project.setProjectStartDate(LocalDate.now().minusDays(1));
        project.setProjectDeadline(LocalDate.now().plusDays(5));
        SubProject s1 = new SubProject(); s1.setSubProjectTimeEstimate(8); s1.setSubProjectTimeSpent(4);
        SubProject s2 = new SubProject(); s2.setSubProjectTimeEstimate(2); s2.setSubProjectTimeSpent(1);
        List<SubProject> subs = List.of(s1, s2);
        // Calculate summary
        TimeSummary summary = timeCalcService.calculateProjectTimeSummary(project, subs);
        // Verify totals (8+2=10 estimate, 4+1=5 spent) and that onTrack is true (since time spent is within expected range before deadline)
        assertEquals(10, summary.getTotalTimeEstimate());
        assertEquals(5, summary.getTotalTimeSpent());
        assertTrue(summary.isOnTrack());
    }
}