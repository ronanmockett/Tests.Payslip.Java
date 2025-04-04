package com.mhrglobal.pay.strategy;

public class ManagerBillingStrategy implements BillingStrategy {
    public double getOvertimeRatePercentage() {
        return 0.25;
    }

    public float getHourlyRate() {
        return 6f;
    }
}
