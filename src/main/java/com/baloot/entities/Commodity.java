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
    private double rating;
    @Getter
    private int inStock;
    @Getter
    private int providerId;
    @Getter
    private transient List<CommodityRate> ratings;

    public Commodity(int id, String name, int price, List<String> categories, int inStock, int providerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = 0;
        this.inStock = inStock;
        this.providerId = providerId;
        this.categories = new ArrayList<>(categories);
        this.ratings = new ArrayList<>();
    }

    public double getRating() {
        if (this.ratings.size() == 0)
            return 0;
        double res = 0;
        for (var rating : this.ratings) {
            res += rating.getRating();
        }
        return res / this.ratings.size();
    }

    public List<String> getCategoryList(String categories) {
        var temp = categories.substring(0, categories.length() - 2);
        return Arrays.stream(temp.split("[, ]")).toList();
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

    public boolean isInList(String category){
        return categories.contains(category);
    }
}
