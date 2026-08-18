package com.ramonda.allo_backend.util;

public class Unicode {
    public static int sumUnicode(String input) {
        int sum = 0;
        for (char c : input.toCharArray()) {
            sum += c;
        }
        return sum;
    }

    public int serviceChargePct(String input){
        return (sumUnicode(input) % 10);
    }
}
