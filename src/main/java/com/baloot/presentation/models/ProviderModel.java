package com.baloot.presentation.models;

import com.baloot.core.entities.Provider;
import com.baloot.service.models.CommodityItemModel;

import java.util.List;

public record ProviderModel(String name, String image, String registryDate, List<CommodityItemModel> commodities) {
}
