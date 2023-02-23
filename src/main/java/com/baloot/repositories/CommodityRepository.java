package com.baloot.repositories;

import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import com.baloot.responses.Response;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommodityRepository {
    @SerializedName("commoditiesList")
    private final List<Commodity> commodities;
    private Gson gson;

    public CommodityRepository() {
        commodities = new ArrayList<>();
        gson = new Gson();
    }

    public Response<String> addCommodity(Commodity commodity) {
        var existingCommodity = findCommodity(commodity.getId());
        if (existingCommodity != null)
            return new Response<>(false, "Commodity already exists.");
        commodities.add(commodity);
        return new Response<>(true, "Commodity added.");
    }

    public Response<String> getCommodities() {
        return new Response<>(true, gson.toJson(commodities));
    }

    public Commodity findCommodity(int commodityId) {
        for (Commodity c : commodities)
            if (c.getId() == commodityId)
                return c;
        return null;
    }

    public Response<String> getCommoditiesByCategory(String category){
        var commoditiesListByCategory = new ArrayList<Commodity>();
        for (Commodity c:commodities) {
            if (c.isInList(category))
                commoditiesListByCategory.add(c);
        }
        return new Response<>(true, gson.toJson(commoditiesListByCategory));
    }
}
