package com.crypto.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class FetchDataService {
    public List<Animal> getAnimalList(){
        List<Animal> list = new ArrayList<Animal>();
        int count = 0;
        int limit = 30;

        Animal[] animals = parseJson();
        for(Animal animal:animals){
            if(count >= limit){
                break;
            }
            if(check(animal)){
                list.add(animal);
                count ++;
            }
        }
        return list;
    }

    public Animal[] parseJson(){
        String url = "https://data.coa.gov.tw/Service/OpenData/TransService.aspx?UnitId=QcbUEzN6E6DL";

        Animal[] animals = null;
        InputStream is = null;

        try{
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int cp;
            while((cp = rd.read()) != -1){
                sb.append((char)cp);
            }

            Gson gson = new Gson();
            animals = gson.fromJson(sb.toString(), Animal[].class);

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return animals;
    }

    public boolean check(Animal animal){
        if(!"".equals(animal.getAnimal_remark().trim()) &&
                !"".equals(animal.getAlbum_file().trim()) &&
                !"".equals(animal.getAnimal_update().trim()) &&
                !"".equals(animal.getShelter_name().trim()))
            return true;
        else
            return false;
    }
}
