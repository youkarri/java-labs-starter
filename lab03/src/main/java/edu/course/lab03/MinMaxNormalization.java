package edu.course.lab03;

public final class MinMaxNormalization implements NormalizationStrategy {

    @Override
    public double[] normalize(double[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        double min = values[0];
        double max = values[0];
        for (double value : values) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        double range = max - min;
        double[] result = new double[values.length];
        if (range != 0) {
            for (int i = 0; i < values.length; i++) {
                result[i] = (values[i] - min) / range;
            }
        }
        return result;
    }
}