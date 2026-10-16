package edu.course.lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataSampleTest {

    private static DataSample valid() {
        return new DataSample("s1", "cat", SampleStatus.NEW, new double[]{1.0, 2.0, 3.0});
    }

    @Test
    void createsValidSample() {
        DataSample sample = valid();
        assertEquals("s1", sample.getId());
        assertEquals("cat", sample.getLabel());
        assertEquals(SampleStatus.NEW, sample.getStatus());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, sample.getFeatures());
    }

    @Test
    void nullIdThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample(null, "cat", SampleStatus.NEW, new double[]{1}));
    }

    @Test
    void emptyIdThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("", "cat", SampleStatus.NEW, new double[]{1}));
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("   ", "cat", SampleStatus.NEW, new double[]{1}));
    }

    @Test
    void nullLabelThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", null, SampleStatus.NEW, new double[]{1}));
    }

    @Test
    void emptyLabelThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", "", SampleStatus.NEW, new double[]{1}));
    }

    @Test
    void nullStatusThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", "cat", null, new double[]{1}));
    }

    @Test
    void nullFeaturesThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", "cat", SampleStatus.NEW, null));
    }

    @Test
    void emptyFeaturesThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", "cat", SampleStatus.NEW, new double[]{}));
    }

    @Test
    void nanFeatureThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", "cat", SampleStatus.NEW, new double[]{1, Double.NaN}));
    }

    @Test
    void infiniteFeatureThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSample("s1", "cat", SampleStatus.NEW,
                        new double[]{Double.POSITIVE_INFINITY}));
    }

    @Test
    void changeStatusUpdatesStatus() {
        DataSample sample = valid();
        sample.changeStatus(SampleStatus.LABELED);
        assertEquals(SampleStatus.LABELED, sample.getStatus());
    }

    @Test
    void changeStatusToNullThrows() {
        DataSample sample = valid();
        assertThrows(IllegalArgumentException.class, () -> sample.changeStatus(null));
        assertEquals(SampleStatus.NEW, sample.getStatus());
    }

    @Test
    void isReadyOnlyForReadyStatus() {
        DataSample sample = valid();
        assertFalse(sample.isReady());
        sample.changeStatus(SampleStatus.READY);
        assertTrue(sample.isReady());
        sample.changeStatus(SampleStatus.REJECTED);
        assertFalse(sample.isReady());
    }

    @Test
    void averageFeatureIsCorrect() {
        assertEquals(2.0, valid().averageFeature(), 0.0001);
    }

    @Test
    void averageFeatureWithNegativeValues() {
        DataSample sample = new DataSample("s2", "dog", SampleStatus.NEW, new double[]{-1, -2, 0});
        assertEquals(-1.0, sample.averageFeature(), 0.0001);
    }

    @Test
    void constructorCopiesInputArray() {
        double[] input = {1.0, 2.0, 3.0};
        DataSample sample = new DataSample("s1", "cat", SampleStatus.NEW, input);
        input[0] = 100.0;
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, sample.getFeatures());
    }

    @Test
    void getterReturnsCopy() {
        DataSample sample = valid();
        double[] returned = sample.getFeatures();
        returned[0] = 100.0;
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, sample.getFeatures());
    }
}