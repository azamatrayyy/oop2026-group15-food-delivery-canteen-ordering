package edu.aitu.oop3.services.pricing;

public class PricingRules {
    private static PricingRules instance;

    private PricingRules() {}

    public static PricingRules getInstance() {
        if (instance == null) {
            instance = new PricingRules();
        }
        return instance;
    }

    public BigDecimal calculateTotal(BigDecimal subtotal) {
        return subtotal.multiply(new BigDecimal("1.12"));
    }
}

