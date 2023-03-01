package com.baloot.repositories;

import com.baloot.entities.User;

import java.util.Objects;

public class UserRepository {
    private final Database database;

    public UserRepository(Database database){
        this.database = database;
    }

    public void addUser(User user){
        var existingUser = findUser(user.getUsername());
        if (existingUser != null)
            updateUser(existingUser, user);
        else
            database.getUsers().add(user);
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
}
