package edu.course.lab08;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsFunctionTest {

    private final StatsFunction function = new StatsFunction();

    @Test
    void computesSumAndAverage() {
        Stats stats = function.apply(new double[] {1.0, 2.0, 3.0, 4.0});
        assertEquals(10.0, stats.sum(), 1e-9);
        assertEquals(2.5, stats.average(), 1e-9);
    }

    @Test
    void singleValue() {
        Stats stats = function.apply(new double[] {7.0});
        assertEquals(7.0, stats.sum(), 1e-9);
        assertEquals(7.0, stats.average(), 1e-9);
    }

    @Test
    void rejectsNullAndEmptyPart() {
        assertThrows(IllegalArgumentException.class, () -> function.apply(null));
        assertThrows(IllegalArgumentException.class, () -> function.apply(new double[0]));
    }

    @Test
    void rejectsNaNAndInfinity() {
        assertThrows(IllegalArgumentException.class, () -> function.apply(new double[] {1.0, Double.NaN}));
        assertThrows(IllegalArgumentException.class,
                () -> function.apply(new double[] {Double.POSITIVE_INFINITY}));
    }
}
