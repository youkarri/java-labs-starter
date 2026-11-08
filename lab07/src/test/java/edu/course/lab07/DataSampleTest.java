package edu.course.lab07;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSampleTest {

    @Test
    void createsValidSample() {
        DataSample sample = new DataSample("S-1", "cat", "camera-1", 128, SampleStatus.READY);
        assertEquals("S-1", sample.getId());
        assertEquals("cat", sample.getLabel());
        assertEquals("camera-1", sample.getSource());
        assertEquals(128, sample.getFeatureCount());
        assertEquals(SampleStatus.READY, sample.getStatus());
    }

    @Test
    void rejectsBlankSource() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("S-1", "cat", " ", 1, SampleStatus.NEW));
    }

    @Test
    void rejectsNonPositiveFeatureCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("S-1", "cat", "camera-1", 0, SampleStatus.NEW));
    }
}
