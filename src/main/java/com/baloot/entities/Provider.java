package com.baloot.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Provider {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private String registryDate;
    @Getter
    private float rating;
    @Getter
    private List<Commodity> commodityList;

    public Provider(int id, String name, String registryDate) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
        rating = 0;
        commodityList = new ArrayList<>();
    }

    public void addCommodity(Commodity commodity){
        rating *= commodityList.size();
        rating += commodity.getRating();
        commodityList.add(commodity);
        rating /= commodityList.size();
    }
}
