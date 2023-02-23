package com.baloot.repositories;

import com.baloot.entities.Commodity;
import com.baloot.entities.User;
import com.baloot.responses.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommodityRepository {
    private final List<Commodity> commodities;

    public CommodityRepository() {
        commodities = new ArrayList<>();
    }

    public Response<String> addCommodity(Commodity commodity){
        var existingUser = findCommodity(commodity.getId());
        if (existingUser != null)
            return new Response<>(false, "Commodity already exists.");
        commodities.add(commodity);
        return new Response<>(true, "User added.");
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public Commodity findCommodity(int commodityId){
        for (Commodity c: commodities)
            if (c.getId() == commodityId)
                return c;
        return null;
    }
}
