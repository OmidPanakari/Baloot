package com.baloot.repositories;

import com.baloot.entities.User;
import com.baloot.responses.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRepository {
    private final List<User> users;

    public UserRepository(){
        users = new ArrayList<>();
    }

    public Response<String> addUser(User user){
        if (!isUserValid(user)){
            return new Response<>(false, "User fields are not valid!");
        }
        var existingUser = findUser(user.getUsername());
        if (existingUser != null)
            updateUser(existingUser, user);
        else
            users.add(user);
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
        for (User u: users)
            if (Objects.equals(u.getUsername(), username))
                return u;
        return null;
    }

    private boolean isUserValid(User user){
        Pattern userNamePattern = Pattern.compile("[a-zA-z0-9]+");
        Matcher userNameMatcher = userNamePattern.matcher(user.getUsername());
        return userNameMatcher.matches();
    }
}
