package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeanCenteringNormalizationTest {

    private final NormalizationStrategy strategy = new MeanCenteringNormalization();

    @Test
    void subtractsMean() {
        assertArrayEquals(new double[]{-1, 0, 1},
                strategy.normalize(new double[]{1, 2, 3}), 1e-9);
    }

    @Test
    void worksWithUnevenValues() {
        assertArrayEquals(new double[]{-3, -1, 4},
                strategy.normalize(new double[]{2, 4, 9}), 1e-9);
    }

    @Test
    void doesNotModifyInput() {
        double[] input = {2, 4, 9};
        strategy.normalize(input);
        assertArrayEquals(new double[]{2, 4, 9}, input, 1e-9);
    }

    @Test
    void rejectsNullArray() {
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }

    @Test
    void rejectsEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(new double[]{}));
    }
}