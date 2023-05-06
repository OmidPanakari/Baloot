package com.baloot.presentation.models;

import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityItem;

import java.util.List;

public record CartModel(List<CommodityItem> buyList, List<CommodityItem> purchasedList) {
}
