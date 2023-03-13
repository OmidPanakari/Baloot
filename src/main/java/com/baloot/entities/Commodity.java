package com.baloot.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Commodity {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private int price;
    @Getter
    private List<String> categories;
    @Getter
    private double rating;
    @Getter @Setter
    private int inStock;
    @Getter
    private int providerId;
    private transient List<CommodityRate> ratings;
    private transient List<Comment> comments;

    public List<CommodityRate> getRatings() {
        if (ratings == null)
            ratings = new ArrayList<>();
        return ratings;
    }

    public List<Comment> getComments() {
        if (comments == null)
            comments = new ArrayList<>();
        return comments;
    }

    public Commodity(Commodity commodity) {
        id = commodity.id;
        name = commodity.name;
        price = commodity.price;
        rating = 0;
        inStock = commodity.inStock;
        providerId = commodity.providerId;
        categories = new ArrayList<>(commodity.categories);
        ratings = new ArrayList<>();
        comments = new ArrayList<>();
    }
    public double getRating() {
        if (ratings == null)
            ratings = new ArrayList<>();
        if (ratings.size() == 0)
            return 0;
        double res = 0;
        for (var rating : ratings) {
            res += rating.getRating();
        }
        return res / ratings.size();
    }

    public String getCategoryString() {
        StringBuilder result = new StringBuilder();
        for (var category:categories){
            result.append(category).append(", ");
        }
        return result.toString();
    }

    public void addRating(CommodityRate commodityRate) {
        for (int i = 0; i < ratings.size(); i++) {
            if (Objects.equals(ratings.get(i).getUsername(), commodityRate.getUsername())) {
                ratings.set(i, commodityRate);
                return;
            }
        }
        ratings.add(commodityRate);
    }
    public void addComment(Comment comment){
        if (comments == null)
            comments = new ArrayList<>();
        comments.add(comment);
    }
    public boolean isInList(String category){
        return categories.contains(category);
    }
}
