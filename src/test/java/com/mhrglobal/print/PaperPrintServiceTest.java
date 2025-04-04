package com.mhrglobal.print;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PaperPrintServiceTest {


    Logger loggerSpy = spy(LoggerFactory.getLogger(PayslipPrintService.class));

    @Test
    public void testPrint() {
        PrintService payslipPrintService = new PayslipPrintService(loggerSpy);
        payslipPrintService.requestPrinting("Engineer", "100", "50", "150");
        payslipPrintService.requestPrinting("Manager", "120","0", "120");

        verify(loggerSpy, times(1)).info(
            "Print called, Role: {}, BasePay: £{}, Overtime: £{}, Total: £{}",
            "Engineer",
            "100",
            "50",
            "150"
        );
        verify(loggerSpy, times(1)).info(
            "Print called, Role: {}, BasePay: £{}, Overtime: £{}, Total: £{}",
            "Manager",
            "120",
            "0",
            "120"
        );
    }

}