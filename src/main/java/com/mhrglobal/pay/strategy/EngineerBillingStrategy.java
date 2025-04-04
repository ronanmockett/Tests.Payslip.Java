package com.mhrglobal.pay.strategy;

public class EngineerBillingStrategy implements BillingStrategy {
    public double getOvertimeRatePercentage() {
        return 0.35;
    }

    public float getHourlyRate() {
        return 5f;
    }
}

