package com.mhrglobal.pay.domain;

import com.mhrglobal.pay.strategy.BillingStrategy;
import com.mhrglobal.pay.strategy.DirectorBillingStrategy;
import com.mhrglobal.pay.strategy.EngineerBillingStrategy;
import com.mhrglobal.pay.strategy.ManagerBillingStrategy;

public class Employee {
    private final Role role;
    private final BillingStrategy billingStrategy;
    
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
        return billingStrategy.getOvertimeRate();
    }
}
