package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoDiscountTest {

    @Test
    void returnsOriginalPrice() {
        assertEquals(100.0, new NoDiscount().apply(100.0), 1e-9);
    }

    @Test
    void returnsZeroForZeroPrice() {
        assertEquals(0.0, new NoDiscount().apply(0.0), 1e-9);
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new NoDiscount().apply(-1.0));
    }
}