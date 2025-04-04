package com.mhrglobal.pay.domain;

import com.mhrglobal.pay.strategy.BillingStrategy;
import com.mhrglobal.pay.strategy.DirectorBillingStrategy;
import com.mhrglobal.pay.strategy.EngineerBillingStrategy;
import com.mhrglobal.pay.strategy.ManagerBillingStrategy;

public class Employee {
    Role role;
    BillingStrategy billingStrategy;
    
    public enum Role {
        Engineer,
        Manager,
        Director
    }

    public Employee(Role role) {
        this.role = role;

        this.billingStrategy = switch (role) {
            case Engineer -> new EngineerBillingStrategy();
            case Manager -> new ManagerBillingStrategy();
            case Director -> new DirectorBillingStrategy();
        };
    }

    public float getHourlyRate() {
        return billingStrategy.getHourlyRate();
    }

    public float getOvertimeRate() {
        var hourlyRate = billingStrategy.getHourlyRate();
        var overtimeRatePercentage = billingStrategy.getOvertimeRatePercentage();

        if ( role.equals(Role.Director) ) {
            return 0;
        }

        return (float) (hourlyRate + (hourlyRate * overtimeRatePercentage));
    }
}
