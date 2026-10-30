package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinMaxNormalizationTest {

    private final NormalizationStrategy strategy = new MinMaxNormalization();

    @Test
    void scalesValuesToZeroOne() {
        assertArrayEquals(new double[]{0.0, 0.5, 1.0},
                strategy.normalize(new double[]{1, 2, 3}), 1e-9);
    }

    @Test
    void worksWithNegativeValues() {
        assertArrayEquals(new double[]{0.0, 0.5, 1.0},
                strategy.normalize(new double[]{-10, 0, 10}), 1e-9);
    }

    @Test
    void constantValuesGiveZeros() {
        assertArrayEquals(new double[]{0, 0, 0},
                strategy.normalize(new double[]{5, 5, 5}), 1e-9);
    }

    @Test
    void doesNotModifyInput() {
        double[] input = {4, 8, 12};
        strategy.normalize(input);
        assertArrayEquals(new double[]{4, 8, 12}, input, 1e-9);
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