package edu.course.lab03;

public final class PriceCalculator {

    private final DiscountPolicy policy;

    public PriceCalculator(DiscountPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("policy must not be null");
        }
        this.policy = policy;
    }

    public double finalPrice(double price) {
        return policy.apply(price);
    }
}