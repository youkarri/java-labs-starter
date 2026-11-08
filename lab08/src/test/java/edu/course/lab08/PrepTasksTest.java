package edu.course.lab08;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrepTasksTest {

    @Test
    void returnsResultsInSubmissionOrder() throws Exception {
        assertEquals(List.of(10, 20, 30), PrepTasks.runThreeTasks());
    }
}
