package com.baloot.service.models;

import lombok.Getter;

public class UserCommodityModel{
    public UserCommodityModel(String username, int commodityId) {
        this.username = username;
        this.commodityId = commodityId;
    }

    @Getter
    private String username;
    @Getter
    private int commodityId;
}