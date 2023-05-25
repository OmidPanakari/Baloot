package com.baloot.presentation.models;

import com.baloot.core.entities.BuyListItem;
import com.baloot.core.entities.PurchasedItem;
import com.baloot.service.models.CommodityItemModel;

import java.util.List;

public record CartModel(List<CommodityItemModel> buyList, List<CommodityItemModel> purchasedList) {
}
