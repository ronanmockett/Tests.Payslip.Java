package com.mhrglobal.pay;

import com.mhrglobal.pay.domain.Employee;
import com.mhrglobal.pay.domain.Payslip;
import com.mhrglobal.payment.BankPaymentService;
import com.mhrglobal.payment.PaymentService;
import com.mhrglobal.print.PayslipPrintService;
import com.mhrglobal.print.PrintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enable Mockito for JUnit 5
class PayServiceTest {
    PayService payService;
    PaymentService paymentServiceSpy = spy(new BankPaymentService());
    PrintService printServiceSpy = spy(new PayslipPrintService());

    @BeforeEach
    void setUp() {
        payService = new PayService(paymentServiceSpy, printServiceSpy);
    }


    @Test
    void testCalculatePayment_LazyEmployee() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Engineer, 0, (short) 20);
        assertEquals(100.00, payslip.total(), "Test employee is paid for minimum contracted hours");
    }

    @Test
    void testCalculatePayment_EmployeeExceededMaximumWorkingHours() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Engineer, 65f, (short) 20);
        assertEquals(60, payslip.baseHours() + payslip.overtimeHours(), "Test payslip only paid for 60 hours");
        assertEquals(370.00, payslip.total(), "Test payslip only paid for 60 hours");
    }

    @Test
    void testCalculatePayment_Engineer() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Engineer, 30f, (short) 20);
        assertEquals(167.50, payslip.total(), "Test calculate pay returns correct total for an Engineer");
    }

    @Test
    void testCalculatePayment_Manager() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Manager, 20f, (short) 20);
        assertEquals(120.00, payslip.total(), "Test calculate pay returns correct total for a Manager");
    }

    @Test
    void testCalculatePayment_Director() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Director, 20f, (short) 20);
        assertEquals(140.00, payslip.total(), "Test calculate pay returns correct total for a Director");
    }

    @Test
    void testCalculatePayment_EngineerWithOvertime() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Engineer, 40f, (short) 20);
        assertEquals(235.00, payslip.total(), "Test calculate pay returns correct total for an Engineer with Overtime");
    }

    @Test
    void testCalculatePayment_ManagerWithOvertime() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Manager, 40f, (short) 20);
        assertEquals(270, payslip.total(), "Test calculate pay returns correct total for a Manager with Overtime");
    }

    @Test
    void testCalculatePayment_DirectorWithOvertime() {
        Payslip payslip = payService.calculatePayment(Employee.Role.Director, 40f, (short) 20);
        assertEquals(0, payslip.overtimePay(), "Test calculate pay does not pay Director for Overtime");
        assertEquals(140, payslip.total(), "Test calculate pay returns correct total for a Director with Overtime");
    }


    @Test
    void testSendPayment_Success() {
        var role = Employee.Role.Director;
        var result = payService.sendPayment(role, 20f, (short) 20);
        assertTrue(result, "test send payment is successful");
        verify(paymentServiceSpy, times(1)).requestPayment(140.0f);
        verify(printServiceSpy, times(1)).requestPrinting(role.toString(), "140.0", "0.0", "140.0");
    }

    @Test
    void testSendPayment_Failed() {
        when(paymentServiceSpy.requestPayment(anyFloat())).thenReturn(false);
        var role = Employee.Role.Director;
        var result = payService.sendPayment(role, 20f, (short) 20);
        assertFalse(result, "test send payment was no successful");
        verify(paymentServiceSpy, times(1)).requestPayment(140.0f);
        verify(printServiceSpy, times(0)).requestPrinting(any(), any() , any(),any());
    }
}