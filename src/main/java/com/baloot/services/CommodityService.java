package com.baloot.services;

import com.baloot.entities.Commodity;
import com.baloot.entities.CommodityRate;
import com.baloot.models.CategoryModel;
import com.baloot.models.IdModel;
import com.baloot.repositories.CommodityRepository;
import com.baloot.repositories.ProviderRepository;
import com.baloot.repositories.UserRepository;
import com.baloot.responses.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommodityService {
    private final ProviderRepository providerRepository;
    private final CommodityRepository commodityRepository;
    private final UserRepository userRepository;
    private final Gson gson;

    public CommodityService(ProviderRepository providerRepository, CommodityRepository commodityRepository, UserRepository userRepository) {
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
        this.userRepository = userRepository;
        gson = new GsonBuilder().create();
    }

    public String addCommodity(String data) {
        Commodity commodity = new Commodity(gson.fromJson(data, Commodity.class));
        var tempProvider = providerRepository.findProvider(commodity.getProviderId());
        if (tempProvider == null)
            return gson.toJson(new Response<>(false, "Provider not found!"));
        return gson.toJson(this.commodityRepository.addCommodity(commodity));
    }

    public String getCommodityById(String data) {
        IdModel idModel = gson.fromJson(data, IdModel.class);
        Commodity commodityToFind = commodityRepository.findCommodity(idModel.getId());
        if (commodityToFind == null)
            return gson.toJson(new Response<>(false, "Commodity not found!"));
        return gson.toJson(new Response<>(true, commodityToFind));
    }

    public String getCommodities() {
        return gson.toJson(this.commodityRepository.getCommodities());
    }

    public String getCommoditiesByCategory(String data) {
        CategoryModel categoryModel = gson.fromJson(data, CategoryModel.class);
        return gson.toJson(commodityRepository.getCommoditiesByCategory(categoryModel.getCategory()));
    }

    public String rateCommodity(String data) {
        CommodityRate commodityRate = gson.fromJson(data, CommodityRate.class);
        Commodity commodityToRate = commodityRepository.findCommodity(commodityRate.getCommodityId());
        if (commodityToRate == null)
            return gson.toJson(new Response<>(false, "Commodity not found!"));
        if (userRepository.findUser(commodityRate.getUsername()) == null)
            return gson.toJson(new Response<>(false, "User not found!"));
        commodityToRate.addRating(commodityRate);
        return gson.toJson(new Response<>(true, "Rate added."));
    }
}
