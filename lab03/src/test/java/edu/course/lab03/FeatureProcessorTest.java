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

    private static class ConstantNormalization implements NormalizationStrategy {
        @Override
        public double[] normalize(double[] values) {
            return new double[]{42};
        }
    }

    @Test
    void usesMeanCenteringStrategy() {
        FeatureProcessor processor = new FeatureProcessor(new MeanCenteringNormalization());
        assertArrayEquals(new double[]{-1, 0, 1},
                processor.process(new double[]{1, 2, 3}), 1e-9);
    }

    @Test
    void strategiesAreReplaceable() {
        double[] input = {2, 4, 6};
        double[] minMax = new FeatureProcessor(new MinMaxNormalization()).process(input);
        double[] centered = new FeatureProcessor(new MeanCenteringNormalization()).process(input);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, minMax, 1e-9);
        assertArrayEquals(new double[]{-2, 0, 2}, centered, 1e-9);
    }

    @Test
    void worksWithNewImplementationWithoutChanges() {
        FeatureProcessor processor = new FeatureProcessor(new ConstantNormalization());
        assertArrayEquals(new double[]{42}, processor.process(new double[]{1, 2, 3}), 1e-9);
    }
}