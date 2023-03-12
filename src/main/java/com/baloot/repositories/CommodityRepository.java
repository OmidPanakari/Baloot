package com.baloot.repositories;

import com.baloot.entities.Commodity;

import java.util.ArrayList;
import java.util.List;

public class CommodityRepository {
    private final Database database;

    public CommodityRepository(Database database) {
        this.database = database;
    }

    public boolean addCommodity(Commodity commodity) {
        var existingCommodity = findCommodity(commodity.getId());
        if (existingCommodity != null)
            return false;
        database.getCommodities().add(commodity);
        return true;
    }

    public List<Commodity> getCommodities() {
        return database.getCommodities();
    }

    public Commodity findCommodity(int commodityId) {
        for (Commodity c : database.getCommodities())
            if (c.getId() == commodityId)
                return c;
        return null;
    }

    public List<Commodity> getCommoditiesByCategory(String category){
        var commoditiesListByCategory = new ArrayList<Commodity>();
        for (Commodity c : database.getCommodities()) {
            if (c.isInList(category))
                commoditiesListByCategory.add(c);
        }
        return commoditiesListByCategory;
    }

    public List<Commodity> getCommoditiesByPrice(int startPrice, int endPrice){
        var commoditiesListByCategory = new ArrayList<Commodity>();
        for (Commodity c : database.getCommodities()) {
            if (c.getPrice() >= startPrice && c.getPrice() <= endPrice)
                commoditiesListByCategory.add(c);
        }
        return commoditiesListByCategory;
    }
}
