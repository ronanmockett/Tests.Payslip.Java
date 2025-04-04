package com.mhrglobal.pay.domain;

public record Payslip(
        Employee.Role role,
        float baseHours,
        float overtimeHours,
        float basePay,
        float overtimePay,
        float total
) {}
