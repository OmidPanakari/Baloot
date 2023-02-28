package com.baloot.repositories;

import com.baloot.entities.Commodity;
import com.baloot.responses.Response;

import java.util.ArrayList;
import java.util.List;

public class CommodityRepository {
    private final Database database;

    public CommodityRepository(Database database) {
        this.database = database;
    }

    public Response<String> addCommodity(Commodity commodity) {
        var existingCommodity = findCommodity(commodity.getId());
        if (existingCommodity != null)
            return new Response<>(false, "Commodity already exists.");
        database.getCommodities().add(commodity);
        return new Response<>(true, "Commodity added.");
    }

    public Response<List<Commodity>> getCommodities() {
        return new Response<>(true, database.getCommodities());
    }

    public Commodity findCommodity(int commodityId) {
        for (Commodity c : database.getCommodities())
            if (c.getId() == commodityId)
                return c;
        return null;
    }

    public Response<List<Commodity>> getCommoditiesByCategory(String category){
        var commoditiesListByCategory = new ArrayList<Commodity>();
        for (Commodity c : database.getCommodities()) {
            if (c.isInList(category))
                commoditiesListByCategory.add(c);
        }
        return new Response<>(true, commoditiesListByCategory);
    }
}
