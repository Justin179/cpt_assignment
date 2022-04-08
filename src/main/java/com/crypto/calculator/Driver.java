package com.crypto.calculator;

import java.util.List;

public class Driver {
    public static void main(String[] args) {
        FetchDataService service = new FetchDataService();
        List<Animal> animalList = service.getAnimalList();
        System.out.println(animalList);

    }
}
