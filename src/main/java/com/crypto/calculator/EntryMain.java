package com.crypto.calculator;

import java.math.BigDecimal;

public class EntryMain {
    /*
    Goal: design a simple program to help Alice to calculate her actual rewards in USD.
        step 1: calculate the daily based vwap in the first 365 days since the employee joined the company.
        step 2: identified how many CROs are to be granted (according to vwap)
        step 3: calculate how much USD is to be granted (depends on the CRO price of the day)
        step 4: calculate how much USD is to be granted with each anniversary's cap
     */
    public static void main(String[] args) throws Exception {
        //  This service calculate daily based VWAP(Volume Weighted Average Price)
        ChangeMoneyService service = new ChangeMoneyService();

        int numberOfDays = 365; // today is her 1st year anniversary, so trace back 365 days
        // VWAP can be calculated with the public candlestick API
        String apiUrl = "https://api.crypto.com/v2/public/get-candlestick?instrument_name=CRO_USDT&timeframe=1D";
        BigDecimal vwap = service.getVWAP(numberOfDays, apiUrl);
        System.out.println("vwap: " + vwap);

        double X = 100d; // Alice Wong's base yearly package of X US dollars
        BigDecimal CROs = service.exchangeCROs(X, vwap);
        System.out.println("CROs: " + CROs);

        // 1 CRO = 0.4377 USD -> can be found in https://crypto.com/price/crypto-com-coin
        double exchangeRateOfTheDay = 0.4377d; // an arbitrary number
        BigDecimal usd = service.cashOutUSD(CROs, exchangeRateOfTheDay);
        System.out.println("uncapped USD: " + usd);

        // today is her 1st year anniversary -> 10% cap
        int year = 1;
        BigDecimal cappedUSD = service.cappedUSD(year,usd);
        System.out.println("capped USD: " + cappedUSD +" (Alice Wong's actual rewards in USD)");
    }
}
