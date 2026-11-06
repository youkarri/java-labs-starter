package edu.course.lab03;

public final class ZScoreNormalization implements NormalizationStrategy {

    @Override
    public double[] normalize(double[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        double mean = sum / values.length;

        double squares = 0;
        for (double value : values) {
            squares += (value - mean) * (value - mean);
        }
        double deviation = Math.sqrt(squares / values.length);

        double[] result = new double[values.length];
        if (deviation != 0) {
            for (int i = 0; i < values.length; i++) {
                result[i] = (values[i] - mean) / deviation;
            }
        }
        return result;
    }
}