package com.baloot.entities;

import lombok.Getter;

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
    private double rating;
    private transient List<Commodity> commodities;

    public List<Commodity> getCommodities() {
        if (commodities == null)
            commodities = new ArrayList<>();
        return commodities;
    }

    public Provider(Provider provider) {
        id = provider.id;
        name = provider.name;
        registryDate = provider.registryDate;
        rating = 0;
        commodities = new ArrayList<>();
    }

    public double getRating() {
        if (this.commodities.size() == 0)
            return 0;
        double res = 0;
        for (var commodity: this.commodities) {
            res += commodity.getRating();
        }
        return res / this.commodities.size();
    }

    public void addCommodity(Commodity commodity){
        if (commodities == null)
            commodities = new ArrayList<>();
        rating *= commodities.size();
        rating += commodity.getRating();
        commodities.add(commodity);
        rating /= commodities.size();
    }
}
