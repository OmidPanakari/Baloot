package com.baloot.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BuyList {
    public BuyList() {
        this.buyList = new ArrayList<>();
    }
    @Getter
    private List<Commodity> buyList;
}
