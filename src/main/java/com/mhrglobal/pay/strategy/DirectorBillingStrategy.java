package com.mhrglobal.pay.strategy;

public class DirectorBillingStrategy implements BillingStrategy {
    public double getOvertimeRatePercentage() {
        return 0;
    }

    public float getHourlyRate() {
        return 7f;
    }
}