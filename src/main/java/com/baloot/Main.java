package com.baloot;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Pattern userNamePattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher userNameMatcher = userNamePattern.matcher("___");
        System.out.println(userNameMatcher.matches());
        String str = "Technology, Phone, awda, wadwad, dawdawdwad, awdawdwa";
        String[] arrOfStr = str.split("[, ]");

        for (String a : arrOfStr)
            System.out.println(a);
    }
}