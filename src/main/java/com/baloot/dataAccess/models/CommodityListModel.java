package com.baloot.dataAccess.models;

import com.baloot.core.entities.Commodity;

import java.util.List;

public record CommodityListModel(List<Commodity> commodities, Integer pageCount) {
}

