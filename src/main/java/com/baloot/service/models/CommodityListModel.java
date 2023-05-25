package com.baloot.service.models;

import java.util.List;

public record CommodityListModel(List<CommodityItemModel> commodities, int pageCount) {
}
