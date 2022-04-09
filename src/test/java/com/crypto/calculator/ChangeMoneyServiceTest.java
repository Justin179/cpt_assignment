package com.crypto.calculator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class ChangeMoneyServiceTest {

    ChangeMoneyService service = new ChangeMoneyService();

    @Test
    void getVWAP() {
        String apiUrl = "https://api.crypto.com/v2/public/get-candlestick?instrument_name=CRO_USDT&timeframe=1D";
        BigDecimal vwap = service.getVWAP(2, apiUrl); // dynamic
        BigDecimal expVwap = manualCalculation2Days(); // data fixed
        // manually calculated 2 days and passed the test once
        // params in manualCalculation2Days are fixed, so vwap and expVwap won't match
//        System.out.println(vwap);
//        System.out.println(expVwap);
//        assertEquals(vwap,expVwap);
    }

    @Test
    void exchangeCROs() {
        double X = 100d;
        // vwap: 0.50d -> 0.50 USD for 1 CRO -> should get 200 CRO
        BigDecimal vwap = new BigDecimal(Double.toString(0.50d)).setScale(4, RoundingMode.HALF_UP);
        BigDecimal cros = service.exchangeCROs(X, vwap);
        BigDecimal expCros = new BigDecimal(Double.toString(200d)).setScale(2, RoundingMode.HALF_UP);
        assertEquals(cros, expCros);
    }

    @Test
    void cashOutUSD() {
        BigDecimal cros = new BigDecimal(Double.toString(200d)).setScale(2, RoundingMode.HALF_UP);
        double exchangeRate = 0.5000d;
        // 1 cro = 0.5000 usd -> so 200 cros should cash out 100 usd
        BigDecimal usd = service.cashOutUSD(cros, exchangeRate);
        BigDecimal expUsd = new BigDecimal(Double.toString(100d)).setScale(4, RoundingMode.HALF_UP);
        assertEquals(usd,expUsd);
    }

    @Test
    void cappedUSD() throws Exception {
        BigDecimal usd = new BigDecimal(Double.toString(100d)).setScale(4, RoundingMode.HALF_UP);
        // 2nd year anniversary is bounded by the 20% cap
        // so 100 usd should be 20
        BigDecimal cappedUsd = service.cappedUSD(2, usd);
        BigDecimal expCappedUsd = new BigDecimal(Double.toString(20d)).setScale(4, RoundingMode.HALF_UP);
        assertEquals(cappedUsd,expCappedUsd);
    }

    private BigDecimal manualCalculation2Days() {
        // second from bottom
        double tp_temp2 = (0.45695d + 0.43444d + 0.43574d) / 3;
        BigDecimal tp2 = new BigDecimal(Double.toString(tp_temp2)).setScale(4, RoundingMode.HALF_UP);
        BigDecimal vol2 = new BigDecimal(Double.toString(30497244.265d)).setScale(4, RoundingMode.HALF_UP);
        BigDecimal tpv2 = tp2.multiply(vol2).setScale(4, RoundingMode.HALF_UP);

        // last one
        // h l c
        double tp_temp = (0.44277d + 0.43323d + 0.44094d) / 3;
        BigDecimal tp = new BigDecimal(Double.toString(tp_temp)).setScale(4, RoundingMode.HALF_UP);
        BigDecimal vol = new BigDecimal(Double.toString(10336329.72d)).setScale(4, RoundingMode.HALF_UP);
        BigDecimal tpv = tp.multiply(vol).setScale(4, RoundingMode.HALF_UP);

        //  [cumulative TPV ÷ cumulative volume]
        BigDecimal cumulativeTpv = tpv.add(tpv2);
        BigDecimal cumulativeVol = vol.add(vol2);
        return cumulativeTpv.divide(cumulativeVol, 4, RoundingMode.HALF_UP);
    }
}