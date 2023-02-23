package com.baloot;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();
        Scanner in = new Scanner(System.in);
        while (true){
            var s = in.nextLine();
            if(Objects.equals(s, "quit"))
                break;
            System.out.println(commandHandler.executeCommand(s));
        }
    }
}