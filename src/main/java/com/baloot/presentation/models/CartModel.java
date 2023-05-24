package com.baloot.presentation.models;

import com.baloot.core.entities.BuyListItem;
import com.baloot.core.entities.PurchasedItem;

import java.util.List;

public record CartModel(List<BuyListItem> buyList, List<PurchasedItem> purchasedList) {
}
