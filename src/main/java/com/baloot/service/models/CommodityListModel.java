package com.baloot.service.models;

import java.util.List;

public record CommodityListModel(List<CommodityModel> commodities, int pageCount) {
}
