package com.baloot.entities;

import com.baloot.responses.Response;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BuyList {
    public BuyList() {
        this.buyList = new ArrayList<>();
    }
    @Getter
    private List<Commodity> buyList;

    public Response<String> addToBuyList(Commodity commodity){
        for (Commodity c:buyList) {
            if (commodity.getId() == c.getId())
                return new Response<>(false, "Commodity already exists in the buy list!");
        }
        buyList.add(commodity);
        return new Response<>(true, "Commodity added to the buy list.");
    }

    public Response<String> removeFromBuyList(Commodity commodity) {
        for (Commodity c:buyList) {
            if (commodity.getId() == c.getId()){
                buyList.remove(c);
                return new Response<>(true, "Commodity removed from the buy list!");
            }
        }
        return new Response<>(false, "Commodity does not exist in the buy list.");
    }
}
