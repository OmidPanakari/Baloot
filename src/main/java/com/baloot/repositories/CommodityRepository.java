package com.baloot.repositories;

import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
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

    public Response<String> addCommodity(Commodity commodity, Provider provider) {
        var existingCommodity = findCommodity(commodity.getId());
        if (existingCommodity != null)
            return new Response<>(false, "Commodity already exists.");
        commodities.add(commodity);
        commodity.addProvider(provider);
        return new Response<>(true, "Commodity added.");
    }

    public List<Commodity> getCommodities() {
        return commodities;
    }

    public Commodity findCommodity(int commodityId) {
        for (Commodity c : commodities)
            if (c.getId() == commodityId)
                return c;
        return null;
    }
}
