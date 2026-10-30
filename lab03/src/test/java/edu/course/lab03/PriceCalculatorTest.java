package edu.course.lab03;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

    private static class HalfPrice implements DiscountPolicy {
        @Override
        public double apply(double price) {
            return price / 2;
        }
    }

    @Test
    void usesNoDiscountPolicy() {
        PriceCalculator calculator = new PriceCalculator(new NoDiscount());
        assertEquals(100.0, calculator.finalPrice(100.0), 1e-9);
    }

    @Test
    void usesPercentDiscountPolicy() {
        PriceCalculator calculator = new PriceCalculator(new PercentDiscount(20));
        assertEquals(80.0, calculator.finalPrice(100.0), 1e-9);
    }

    @Test
    void policyCanBeReplaced() {
        PriceCalculator withoutDiscount = new PriceCalculator(new NoDiscount());
        PriceCalculator withDiscount = new PriceCalculator(new PercentDiscount(50));
        assertEquals(200.0, withoutDiscount.finalPrice(200.0), 1e-9);
        assertEquals(100.0, withDiscount.finalPrice(200.0), 1e-9);
    }

    @Test
    void worksWithNewImplementationWithoutChanges() {
        PriceCalculator calculator = new PriceCalculator(new HalfPrice());
        assertEquals(30.0, calculator.finalPrice(60.0), 1e-9);
    }

    @Test
    void rejectsNullPolicy() {
        assertThrows(IllegalArgumentException.class, () -> new PriceCalculator(null));
    }
}