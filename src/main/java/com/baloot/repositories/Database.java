package com.baloot.repositories;

import com.baloot.entities.Comment;
import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Database {
    @Getter
    private List<User> users;
    @Getter
    private List<Provider> providers;
    @Getter
    private List<Commodity> commodities;
    @Getter
    private List<Comment> comments;

    public Database() {
        users = new ArrayList<>();
        providers = new ArrayList<>();
        comments = new ArrayList<>();
        commodities = new ArrayList<>();
    }
}
