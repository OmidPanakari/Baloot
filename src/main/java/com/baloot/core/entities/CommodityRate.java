package com.baloot.core.entities;

import lombok.Getter;

public class CommodityRate {
    public CommodityRate(String username, int commodityId, double rating) {
        this.username = username;
        this.commodityId = commodityId;
        this.rating = rating;
    }
    @Getter
    private String username;
    @Getter
    private int commodityId;
    @Getter
    private double rating;
}
