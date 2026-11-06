package edu.course.lab05;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoadResultTest {

    private static DataSample sample() {
        return new DataSample("S-1", "cat", "camera-1", 1, SampleStatus.NEW);
    }

    @Test
    void exposesSamplesAndErrors() {
        ValidationError error = new ValidationError(2, "id", "must not be empty");
        LoadResult result = new LoadResult(List.of(sample()), List.of(error));
        assertEquals(1, result.getSamples().size());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.hasErrors());
    }

    @Test
    void resultIsProtectedFromOutsideChanges() {
        List<DataSample> samples = new ArrayList<>();
        samples.add(sample());
        LoadResult result = new LoadResult(samples, List.of());

        samples.clear();

        assertEquals(1, result.getSamples().size());
        assertFalse(result.hasErrors());
        assertThrows(UnsupportedOperationException.class,
                () -> result.getSamples().add(sample()));
    }
}