package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureProcessorTest {

    @Test
    void usesMinMaxStrategy() {
        FeatureProcessor processor = new FeatureProcessor(new MinMaxNormalization());
        assertArrayEquals(new double[]{0.0, 0.5, 1.0},
                processor.process(new double[]{1, 2, 3}), 1e-9);
    }

    @Test
    void doesNotModifyInput() {
        FeatureProcessor processor = new FeatureProcessor(new MinMaxNormalization());
        double[] input = {1, 2, 3};
        processor.process(input);
        assertArrayEquals(new double[]{1, 2, 3}, input, 1e-9);
    }

    @Test
    void rejectsNullStrategy() {
        assertThrows(IllegalArgumentException.class, () -> new FeatureProcessor(null));
    }

    @Test
    void rejectsEmptyVector() {
        FeatureProcessor processor = new FeatureProcessor(new MinMaxNormalization());
        assertThrows(IllegalArgumentException.class, () -> processor.process(new double[]{}));
    }
}