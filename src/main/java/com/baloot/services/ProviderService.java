package com.baloot.services;

import com.baloot.entities.Provider;
import com.baloot.repositories.ProviderRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProviderService {
    private final ProviderRepository providerRepository;
    private final Gson gson;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
        gson = new GsonBuilder().create();
    }

    public String addProvider(String data) {
        Provider provider = new Provider(gson.fromJson(data, Provider.class));
        return gson.toJson(this.providerRepository.addProvider(provider));
    }
}
