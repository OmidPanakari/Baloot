package com.baloot.dataAccess;

import com.baloot.core.entities.*;
import com.baloot.utils.HibernateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Database {

    public void init() {
        var gson = new Gson();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var usersJson = getUrl("http://5.253.25.110:5000/api/users");
            var element = gson.fromJson(usersJson, JsonElement.class).getAsJsonArray();
            var transaction = session.beginTransaction();
            for (int i = 0; i < element.size(); i++) {
                var userJson = element.get(i).getAsJsonObject();
                var user = new User(
                    userJson.get("password").getAsString(),
                    userJson.get("username").getAsString(),
                    userJson.get("email").getAsString(),
                    userJson.get("address").getAsString(),
                    userJson.get("birthDate").getAsString(),
                    userJson.get("credit").getAsInt());
                session.saveOrUpdate(user);
            }
            transaction.commit();
            var providersJson = getUrl("http://5.253.25.110:5000/api/v2/providers");
            element = gson.fromJson(providersJson, JsonElement.class).getAsJsonArray();
            transaction = session.beginTransaction();
            for (int i = 0; i < element.size(); i++) {
                var providerJson = element.get(i).getAsJsonObject();
                var provider = new Provider(
                    providerJson.get("id").getAsInt(),
                    providerJson.get("name").getAsString(),
                    providerJson.get("registryDate").getAsString(),
                    providerJson.get("image").getAsString());
                session.saveOrUpdate(provider);
            }
            transaction.commit();
            var discountsJson = getUrl("http://5.253.25.110:5000/api/discount");
            element = gson.fromJson(discountsJson, JsonElement.class).getAsJsonArray();
            transaction = session.beginTransaction();
            for (int i = 0; i < element.size(); i++) {
                var discountJson = element.get(i).getAsJsonObject();
                var discount = new Discount(
                    discountJson.get("discountCode").getAsString(),
                    discountJson.get("discount").getAsInt());
                session.saveOrUpdate(discount);
            }
            transaction.commit();
            var commoditiesJson = getUrl("http://5.253.25.110:5000/api/v2/commodities");
            element = gson.fromJson(commoditiesJson, JsonElement.class).getAsJsonArray();
            transaction = session.beginTransaction();
            for (int i = 0; i < element.size(); i++) {
                var commodityJson = element.get(i).getAsJsonObject();
                var commodity = new Commodity(
                    commodityJson.get("id").getAsInt(),
                    commodityJson.get("name").getAsString(),
                    commodityJson.get("price").getAsInt(),
                    commodityJson.get("inStock").getAsInt(),
                    commodityJson.get("providerId").getAsInt(),
                    gson.fromJson(commodityJson.get("categories"), new TypeToken<List<String>>() {}.getType()),
                    commodityJson.get("image").getAsString());
                session.saveOrUpdate(commodity);
            }
            transaction.commit();
            var commentsJson = getUrl("http://5.253.25.110:5000/api/comments");
            element = gson.fromJson(commentsJson, JsonElement.class).getAsJsonArray();
            transaction = session.beginTransaction();
            for (int i = 0; i < element.size(); i++) {
                var commentJson = element.get(i).getAsJsonObject();
                var query = session.createQuery("FROM User u WHERE u.email = :email", User.class);
                query.setParameter("email", commentJson.get("userEmail").getAsString());
                var user = (User) query.uniqueResult();
                var comment = new Comment(
                    user.getUsername(),
                    commentJson.get("commodityId").getAsInt(),
                    commentJson.get("text").getAsString(),
                    commentJson.get("date").getAsString());
                session.saveOrUpdate(comment);
            }
            transaction.commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
