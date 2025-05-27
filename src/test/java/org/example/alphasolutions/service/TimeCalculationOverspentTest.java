package org.example.alphasolutions.service;

import java.time.LocalDate;
import java.util.List;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.TimeSummary;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

class TimeCalculationOverspentTest {

    private final TimeCalculationService svc = new TimeCalculationService();

    @Test
    void projectMarkedNotOnTrackWhenSpentExceedsEstimate() {
        Project project = new Project();
        project.setProjectStartDate(LocalDate.now().minusDays(10));
        project.setProjectDeadline(LocalDate.now().plusDays(10));
        project.setProjectTimeEstimate(6);

        SubProject sp = new SubProject();
        sp.setSubProjectTimeEstimate(6);
        sp.setSubProjectTimeSpent(8);

        TimeSummary summary = svc.calculateProjectTimeSummary(project, List.of(sp));

        assertEquals(6, summary.getTotalTimeEstimate());
        assertEquals(8, summary.getTotalTimeSpent());
        assertFalse(summary.isOnTrack(), "Projektet skal markeres som overskredet");
    }
}