package com.baloot.repositories;

import com.baloot.entities.User;
import com.baloot.responses.Response;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRepository {
    private final Database database;

    public UserRepository(Database database){
        this.database = database;
    }

    public Response<String> addUser(User user){
        var existingUser = findUser(user.getUsername());
        if (existingUser != null)
            updateUser(existingUser, user);
        else
            database.getUsers().add(user);
        return new Response<>(true, "User added.");
    }

    private void updateUser(User oldUser, User newUser){
        oldUser.setCredit(newUser.getCredit());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setAddress(newUser.getAddress());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setBirthDate(newUser.getBirthDate());
    }

    public User findUser(String username){
        for (User u: database.getUsers())
            if (Objects.equals(u.getUsername(), username))
                return u;
        return null;
    }

    private boolean isUserValid(User user){
        Pattern userNamePattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher userNameMatcher = userNamePattern.matcher(user.getUsername());
        return userNameMatcher.matches();
    }
}
