package edu.aitu.oop3.services.pricing;

import java.math.BigDecimal;

public class PricingRules {

    private static PricingRules instance;

    private final BigDecimal TAX_RATE = new BigDecimal("0.12"); // 12%

    private PricingRules() {}

    public static PricingRules getInstance() {
        if (instance == null) {
            instance = new PricingRules();
        }
        return instance;
    }

    public BigDecimal applyTax(BigDecimal basePrice) {
        return basePrice.add(basePrice.multiply(TAX_RATE));
    }
}