package com.baloot.repositories;

import com.baloot.entities.Comment;
import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Database {

    @Getter
    private List<User> users;
    @Getter
    private List<Provider> providers;
    @Getter
    private List<Commodity> commodities;
    @Getter
    private List<Comment> comments;

    public Database() throws IOException {
        var gson = new Gson();
        var userJson = getUrl("http://5.253.25.110:5000/api/users");
        var type = new TypeToken<List<User>>(){}.getType();
        users = gson.fromJson(userJson, type);
        var providersJson = getUrl("http://5.253.25.110:5000/api/providers");
        type = new TypeToken<List<Provider>>(){}.getType();
        providers = gson.fromJson(providersJson, type);
        var commentsJson = getUrl("http://5.253.25.110:5000/api/comments");
        type = new TypeToken<List<Comment>>(){}.getType();
        comments = gson.fromJson(commentsJson, type);
        var commoditiesJson = getUrl("http://5.253.25.110:5000/api/commodities");
        type = new TypeToken<List<Commodity>>(){}.getType();
        commodities = gson.fromJson(commoditiesJson, type);
        comments.forEach(c -> c.setUsername(users.stream()
                .filter(u -> Objects.equals(u.getEmail(), c.getUserEmail()))
                .findFirst().
                get().getUsername()));
        comments.forEach(c -> commodities.stream().filter(co -> co.getId() == c.getCommodityId())
                .findFirst()
                .get().addComment(c));
        commodities.forEach(c -> providers.stream().filter(p -> p.getId() == c.getProviderId())
                .findFirst()
                .get().addCommodity(c));
    }

    private String getUrl(String address) throws IOException {
        var url = new URL(address);
        var con = (HttpURLConnection) url.openConnection();
        var sc = new Scanner(con.getInputStream());
        var response = new StringBuilder();
        while (sc.hasNextLine()) {
            response.append(sc.nextLine());
        }
        return response.toString();
    }
}
