package edu.course.lab03;

public final class NoDiscount implements DiscountPolicy {

    @Override
    public double apply(double price) {
        if (Double.isNaN(price) || price < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        return price;
    }
}