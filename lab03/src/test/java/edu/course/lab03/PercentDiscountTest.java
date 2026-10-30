package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercentDiscountTest {

    @Test
    void appliesTwentyPercentToHundred() {
        assertEquals(80.0, new PercentDiscount(20).apply(100.0), 1e-9);
    }

    @Test
    void zeroPercentKeepsPrice() {
        assertEquals(50.0, new PercentDiscount(0).apply(50.0), 1e-9);
    }

    @Test
    void hundredPercentGivesZero() {
        assertEquals(0.0, new PercentDiscount(100).apply(50.0), 1e-9);
    }

    @Test
    void rejectsNegativePercent() {
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(-1));
    }

    @Test
    void rejectsPercentAboveHundred() {
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(100.5));
    }

    @Test
    void rejectsNegativePrice() {
        PercentDiscount discount = new PercentDiscount(10);
        assertThrows(IllegalArgumentException.class, () -> discount.apply(-5.0));
    }
}