package com.mhrglobal.pay.strategy;

public interface BillingStrategy {
    float getHourlyRate();

    double getOvertimeRatePercentage();

    default float getOvertimeRate() {
        var hourlyRate = getHourlyRate();
        var overtimeRatePercentage = getOvertimeRatePercentage();

        return (float) (hourlyRate + (hourlyRate * overtimeRatePercentage));
    }
}
