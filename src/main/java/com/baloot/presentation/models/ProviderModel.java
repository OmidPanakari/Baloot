package com.baloot.presentation.models;

import com.baloot.core.entities.Provider;
import com.baloot.service.models.CommodityItemModel;

import java.util.List;

public record ProviderModel(Provider provider, List<CommodityItemModel> commodities) {
}
