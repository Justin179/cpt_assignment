package com.crypto.calculator;

import java.math.BigDecimal;

public class Driver {
    public static void main(String[] args) {
        //  This service calculate daily based VWAP(Volume Weighted Average Price)
        VWAPService service = new VWAPService();
        int numberOfDays = 365;
        BigDecimal vwap = service.calculate(numberOfDays);
        System.out.println(vwap);



    }
}
