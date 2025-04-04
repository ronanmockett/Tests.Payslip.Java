package com.mhrglobal.pay.strategy;

public interface BillingStrategy {
    double getOvertimeRatePercentage();
    float getHourlyRate();
}
