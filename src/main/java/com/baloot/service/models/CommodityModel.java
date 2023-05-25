package com.baloot.service.models;

import java.util.List;

public record CommodityModel(int id, String name, int price, String image, double rating, int rateCount, int inStock,
                             String providerName, int providerId, List<String> categories){}
