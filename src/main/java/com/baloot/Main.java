package com.baloot;
import com.google.gson.Gson;
public class Main {
    public static void main(String[] args) {
        String str = "Technology, Phone, awda, wadwad, dawdawdwad, awdawdwa";
        String[] arrOfStr = str.split("[, ]");

        for (String a : arrOfStr)
            System.out.println(a);
    }
}