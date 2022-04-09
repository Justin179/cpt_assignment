package com.crypto.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * This service calculate daily based VWAP(Volume Weighted Average Price)
 */
public class ChangeMoneyService {

    public BigDecimal getVWAP(int numOfDays, String url){
        String rawData = callCandlestickAPI(url);
        JsonArray jsonArray = dataToJsonArray(rawData);
        return calculateVWAP(numOfDays, jsonArray);
    }

    public BigDecimal exchangeCROs(double usd, BigDecimal vwap) {
        BigDecimal dollars = new BigDecimal(Double.toString(usd)).setScale(4, RoundingMode.HALF_UP);
        // https://crypto.com/price/crypto-com-coin -> CRO: two decimal places
        return dollars.divide(vwap,2,RoundingMode.HALF_UP);
    }

    public BigDecimal cashOutUSD(BigDecimal cros, double exchangeRate) {
        BigDecimal exchangeRateOfTheDay = new BigDecimal(Double.toString(exchangeRate)).setScale(4, RoundingMode.HALF_UP);
        return cros.multiply(exchangeRateOfTheDay).setScale(4,RoundingMode.HALF_UP);
    }

    public BigDecimal cappedUSD(int year, BigDecimal usd) throws Exception {
        double cap = 0d;
        switch (year){
            case 1:
                cap = 0.1;
                break;
            case 2:
                cap = 0.2;
                break;
            case 3:
                cap = 0.3;
                break;
            case 4:
                cap = 0.4;
                break;
            default:
                throw new Exception("year out of bound!");
        }
        BigDecimal capRate = new BigDecimal(Double.toString(cap)).setScale(4, RoundingMode.HALF_UP);
        return usd.multiply(capRate).setScale(4,RoundingMode.HALF_UP);
    }

    // -------------------------- private methods below

    private String callCandlestickAPI(String url) {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try{
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            int cp;
            while((cp = rd.read()) != -1)
                sb.append((char)cp);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally{
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private JsonArray dataToJsonArray(String data) {
        Gson gson = new Gson();
        // Convert JSON to JsonElement
        JsonElement jsonElement = gson.fromJson(data, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject result = jsonObject.getAsJsonObject("result");
        return result.getAsJsonArray("data");
    }

    private BigDecimal calculateVWAP(int numOfDays, JsonArray jsonArray) {
        BigDecimal vwap;
        BigDecimal cumulativeTpv = BigDecimal.ZERO;
        BigDecimal cumulativeVol = BigDecimal.ZERO;
        int dataSize = jsonArray.size();
        // the daily based VWAP(Volume Weighted Average Price) in the first 365 days since the employee joined the company.
        for (int i = dataSize-1; i>=dataSize- numOfDays; i--) {
            Data data = new Gson().fromJson(jsonArray.get(i).toString(), Data.class);
            // calculate tpv
            double typicalPrice = (data.getH()+data.getL()+data.getC()) / 3;
            // new BigDecimal(Double.toString(c)) / BigDecimal.valueOf(typicalPrice) either way is fine
            // Notes: do not do -> BigDecimal(double val) The results of this constructor can be somewhat unpredictable.
            BigDecimal tp = new BigDecimal(Double.toString(typicalPrice)).setScale(4, RoundingMode.HALF_UP);
            BigDecimal tpv = tp.multiply(data.getV()).setScale(4, RoundingMode.HALF_UP);
            cumulativeTpv = cumulativeTpv.add(tpv);
            cumulativeVol = cumulativeVol.add(data.getV());
        }
        // Calculate VWAP with your information: [cumulative TPV ÷ cumulative volume]. This will provide a volume-weighted average price for each period
        vwap = cumulativeTpv.divide(cumulativeVol, 4, RoundingMode.HALF_UP);
        // e.g. vwap = 0.3812 = 0.3812 USD for 1 CRO (exchange rate)
        return vwap;
    }
}
