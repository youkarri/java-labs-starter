package edu.course.lab03;

public final class PercentDiscount implements DiscountPolicy {

    private final double percent;

    public PercentDiscount(double percent) {
        if (Double.isNaN(percent) || percent < 0 || percent > 100) {
            throw new IllegalArgumentException("percent must be between 0 and 100");
        }
        this.percent = percent;
    }

    @Override
    public double apply(double price) {
        if (Double.isNaN(price) || price < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        return price * (100 - percent) / 100;
    }
}