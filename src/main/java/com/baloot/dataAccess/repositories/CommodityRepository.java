package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Commodity;
import com.baloot.dataAccess.Database;
import com.baloot.dataAccess.models.CommodityList;
import com.baloot.dataAccess.utils.QueryModel;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public CommodityList getCommodities(QueryModel query) {
        var commodities = database.getCommodities();
        return applyQuery(commodities, query);
    }

    public Commodity findCommodity(int commodityId) {
        for (Commodity c : database.getCommodities())
            if (c.getId() == commodityId)
                return c;
        return null;
    }

//    public List<Commodity> getCommoditiesByPrice(int startPrice, int endPrice, int page, int limit){
//        var commoditiesListByPrice = new ArrayList<Commodity>();
//        for (Commodity c : database.getCommodities()) {
//            if (c.getPrice() >= startPrice && c.getPrice() <= endPrice)
//                commoditiesListByPrice.add(c);
//        }
//        return applyQuery(commoditiesListByPrice, page, limit);
//    }

    private CommodityList applyQuery(List<Commodity> commodities, QueryModel query) {
        if (query == null)
            return new CommodityList(commodities, 1);
        var result = commodities;
        result = applyAvailableFilter(result, query.available());
        result = applySearch(result, query.search(), query.searchType());
        applySort(result, query.sort());
        return applyPagination(result, query.page(), query.limit());
    }

    private List<Commodity> applySearch(List<Commodity> commodities, String search, String searchType) {
        if (search != null) {
            if (Objects.equals(searchType, "category"))
                return commodities.stream().filter(c -> c.isInList(search)).collect(Collectors.toList());
            else if (Objects.equals(searchType, "name"))
                return commodities.stream().filter(c -> c.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
            else if (Objects.equals(searchType, "provider"))
                return commodities.stream().filter(c -> c.getProviderName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return commodities;
    }

    private void applySort(List<Commodity> commodities, String sort) {
        if (Objects.equals(sort, "name")) {
            Comparator<Commodity> byName = Comparator.comparing(Commodity::getName);
            commodities.sort(byName);
        }
        else if (Objects.equals(sort, "price")) {
            Comparator<Commodity> byPrice = Comparator.comparingInt(Commodity::getPrice);
            commodities.sort(byPrice);
        }
    }

    private CommodityList applyPagination(List<Commodity> commodities, Integer page, Integer limit) {
        if (limit == null || limit == 0)
            return new CommodityList(commodities, 1);
        if (page == null || page == 0)
            return new CommodityList(commodities.stream()
                    .limit(limit).collect(Collectors.toList()),
                    (commodities.size() + limit - 1) / limit);
        return new CommodityList(commodities.stream()
                .skip((long) (page - 1) * limit)
                .limit(limit).collect(Collectors.toList()),
                (commodities.size() + limit - 1) / limit);
    }

    private List<Commodity> applyAvailableFilter(List<Commodity> commodities, Boolean available) {
        if (available == null || !available)
            return commodities;
        return commodities.stream().filter(c -> c.getInStock() > 0).collect(Collectors.toList());
    }
}
