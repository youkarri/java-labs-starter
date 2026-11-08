package edu.course.lab08;

import java.util.Arrays;

/**
 * Считает сумму и среднее числового массива. Некорректная часть набора данных
 * (null, пустой массив, NaN или бесконечность) приводит к исключению.
 */
public final class StatsFunction implements TaskFunction<double[], Stats> {

    @Override
    public Stats apply(double[] part) {
        if (part == null) {
            throw new IllegalArgumentException("part must not be null");
        }
        if (part.length == 0) {
            throw new IllegalArgumentException("part must not be empty");
        }
        if (Arrays.stream(part).anyMatch(value -> Double.isNaN(value) || Double.isInfinite(value))) {
            throw new IllegalArgumentException("part contains NaN or infinite value");
        }
        double sum = Arrays.stream(part).sum();
        return new Stats(sum, sum / part.length);
    }
}
