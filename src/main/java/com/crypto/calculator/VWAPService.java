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
public class VWAPService {

    public BigDecimal calculate(int numOfDays){
        String url = "https://api.crypto.com/v2/public/get-candlestick?instrument_name=CRO_USDT&timeframe=1D";
        InputStream is = null;

        BigDecimal vwap = BigDecimal.ZERO;

        try{
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int cp;
            while((cp = rd.read()) != -1){
                sb.append((char)cp);
            }

            Gson gson = new Gson();
            // Convert JSON to JsonElement
            JsonElement jsonElement = gson.fromJson(sb.toString(), JsonElement.class);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject result = jsonObject.getAsJsonObject("result");
            JsonArray jsonArray = result.getAsJsonArray("data");

            BigDecimal cumulativeTpv = BigDecimal.ZERO;
            BigDecimal cumulativeVol = BigDecimal.ZERO;

//            List<Data> dataList = new ArrayList<>();
            int dataSize = jsonArray.size();
            // the daily based VWAP(Volume Weighted Average Price) in the first 365 days since the employee joined the company.
            for (int i = dataSize-1; i>=dataSize-numOfDays; i--) {
                Data data = new Gson().fromJson(jsonArray.get(i).toString(), Data.class);

                // calculate tpv
                double typicalPrice = (data.getH()+data.getL()+data.getC()) / 3;
                BigDecimal tp = new BigDecimal(typicalPrice).setScale(4, RoundingMode.HALF_UP);
                BigDecimal tpv = tp.multiply(data.getV()).setScale(4, RoundingMode.HALF_UP);
                data.setTpv(tpv);

                cumulativeTpv = cumulativeTpv.add(tpv);
                cumulativeVol = cumulativeVol.add(data.getV());

//                dataList.add(data);
            }

            // Calculate VWAP with your information: [cumulative TPV ÷ cumulative volume]. This will provide a volume-weighted average price for each period
            vwap = cumulativeTpv.divide(cumulativeVol, 4, RoundingMode.HALF_UP);
            // e.g. vwap = 0.3812 = 0.3812 USD for 1 CRO (exchange rate)
            System.out.println(vwap);

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return vwap;
    }

}
