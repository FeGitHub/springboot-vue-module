package com.test;

import nl.flotsam.xeger.Xeger;

public class TestUnit {
    public static void main(String[] args){
        String regex = "[0-9]{1,2}";
        Xeger generator = new Xeger(regex);
        for (int i = 0; i < 10; i++) {
            String result = generator.generate();
            System.out.println(result);
        }
    }
}

