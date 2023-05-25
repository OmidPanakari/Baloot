package com.baloot.service.models;

import com.baloot.core.entities.Commodity;

public record CommodityItemModel(CommodityModel commodity, int inCart) {
}
