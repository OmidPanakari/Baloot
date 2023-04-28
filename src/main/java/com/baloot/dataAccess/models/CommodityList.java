package com.baloot.dataAccess.models;

import com.baloot.core.entities.Commodity;

import java.util.List;

public record CommodityList(List<Commodity> commodities, Integer pageCount) {
}

