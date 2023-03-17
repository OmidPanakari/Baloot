package com.baloot.service;

import com.baloot.core.entities.Provider;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;

public class ProviderService {
    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public Response addProvider(Provider provider) {
        if (providerRepository.findProvider(provider.getId()) != null)
            return new DataResponse<>(false, "Provider id is taken!");
        providerRepository.addProvider(provider);
        return new DataResponse<>(true, "Provider added.");
    }

    public Response getProviderById(int providerId) {
        Provider provider = providerRepository.findProvider(providerId);
        if (provider == null) {
            return new DataResponse<>(false, "Provider not found!");
        }
        return new DataResponse<>(true, provider);
    }
}
