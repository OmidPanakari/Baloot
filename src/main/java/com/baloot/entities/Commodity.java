package com.baloot.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public class Commodity {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private int price;
    @Getter
    private String categories;
    @Getter
    private double rating;
    @Getter
    private int inStock;
    @Getter
    private Provider provider;

    public Commodity(int id, String name, int price, String categories, double rating, int inStock){
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.inStock = inStock;
        this.categories = categories;
    }

    public List<String> getCategoryList(String categories){
        var temp = categories.substring(0, categories.length() - 2);
        return Arrays.stream(temp.split("[, ]")).toList();
    }

    public void addProvider(Provider provider){
        this.provider = provider;
        provider.addCommodity(this);
    }
}
