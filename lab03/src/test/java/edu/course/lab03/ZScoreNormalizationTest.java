package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZScoreNormalizationTest {

    private final NormalizationStrategy strategy = new ZScoreNormalization();

    @Test
    void computesZScores() {
        double expected = Math.sqrt(1.5);
        assertArrayEquals(new double[]{-expected, 0, expected},
                strategy.normalize(new double[]{1, 2, 3}), 1e-9);
    }

    @Test
    void constantValuesGiveZeros() {
        assertArrayEquals(new double[]{0, 0, 0},
                strategy.normalize(new double[]{7, 7, 7}), 1e-9);
    }

    @Test
    void doesNotModifyInput() {
        double[] input = {1, 2, 3};
        strategy.normalize(input);
        assertArrayEquals(new double[]{1, 2, 3}, input, 1e-9);
    }

    @Test
    void rejectsEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(new double[]{}));
    }
}