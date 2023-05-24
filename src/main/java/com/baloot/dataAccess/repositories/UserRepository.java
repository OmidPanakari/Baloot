package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.User;
import com.baloot.dataAccess.Database;
import com.baloot.utils.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
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
        Session session = HibernateUtil.getSessionFactory().openSession();
        var user = session.get(User.class, username);
        session.close();
        return user;
    }
}
