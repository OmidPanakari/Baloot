package com.baloot;

import com.baloot.entities.User;
import com.baloot.repositories.UserRepository;
import com.google.gson.Gson;

public class CommandHandler {
    private UserRepository userRepository;

    public CommandHandler() {
        this.userRepository = new UserRepository();
    }

    public String executeCommand(String input) {
        var commandList = input.split(" ", 2);
        var command = commandList[0];
        Gson gson = new Gson();
        var data = commandList.length > 1 ? commandList[1] : "";
        switch (command) {
            case "addUser":
                User user = gson.fromJson(data, User.class);
                var response = this.userRepository.addUser(user);
                return gson.toJson(response);
            default:
                return "Bad";
        }
    }
}
