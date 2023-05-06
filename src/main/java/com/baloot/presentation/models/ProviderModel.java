package com.baloot.presentation.models;

import com.baloot.core.entities.Provider;
import com.baloot.service.models.CommodityModel;

import java.util.List;

public record ProviderModel(Provider provider, List<CommodityModel> commodities) {
}
