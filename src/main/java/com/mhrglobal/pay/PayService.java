package com.mhrglobal.pay;

import com.mhrglobal.pay.domain.Employee;
import com.mhrglobal.pay.domain.Payslip;
import com.mhrglobal.payment.PaymentService;
import com.mhrglobal.print.PrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Implement a Pay Service that makes use of the payment and printing services
public class PayService {

    private final Logger logger = LoggerFactory.getLogger(PayService.class);
    private final PaymentService bankPaymentService;
    private final PrintService paperPrintService;

    public PayService(PaymentService bankPaymentService, PrintService paperPrintService) {
        this.bankPaymentService = bankPaymentService;
        this.paperPrintService = paperPrintService;
    }

    /**
     * Calculates the amount owed and returns an unprinted payslip.
     * @param role The employees roles
     * @param hoursWorked The number of hours worked
     * @param contractedHours The number of hours the employee is contracted for
     * @return payslip
     */
    public Payslip calculatePayment(Employee.Role role, float hoursWorked, short contractedHours) {
        var maximumPaidHours = 60;
        var employee = new Employee(role);

        float payableHoursWorked = Math.min(maximumPaidHours, hoursWorked);
        float overtimeHours = Math.max(0, payableHoursWorked - (float) contractedHours);

        var basePay = contractedHours * employee.getHourlyRate();
        var overtimePay = overtimeHours * employee.getOvertimeRate();
        var total = basePay + overtimePay;

        logger.debug(String.format("calculatePay [hourlyRate: %s, overtimeRate: %s, contractedHours: %s, overtimeHours: %s, basePay: %s, overtimePay: %s, total: %s]",
                        employee.getHourlyRate(),
                        employee.getOvertimeRate(),
                        contractedHours,
                        overtimeHours,
                        basePay,
                        overtimePay,
                        total
                )
        );

        return new Payslip(
                role,
                contractedHours,
                overtimeHours,
                basePay,
                overtimePay,
                total
        );
    }

    /**
     * Calculates the amount owed, makes the payment and prints a payslip.
     * @param role The employees roles
     * @param hoursWorked The number of hours worked
     * @param contractedHours The number of hours the employee is contracted for
     * @return true if successful
     */
    public boolean sendPayment(Employee.Role role, float hoursWorked, short contractedHours) {
        Payslip payslip = calculatePayment(role, hoursWorked, contractedHours);
        var paymentSuccessful = bankPaymentService.requestPayment(payslip.total());

        if (paymentSuccessful) {
            paperPrintService.requestPrinting(
                    role.toString(),
                    Float.toString(payslip.basePay()),
                    Float.toString(payslip.overtimePay()),
                    Float.toString(payslip.total())
            );
        } else {
            // Probably would want to handle a failed transaction.
            return false;
        }
        return true;
    }
}


