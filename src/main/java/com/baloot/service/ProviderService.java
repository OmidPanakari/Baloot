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
            return DataResponse.Failed("Provider id is taken!");
        providerRepository.addProvider(provider);
        return DataResponse.Successful();
    }

    public Response getProviderById(int providerId) {
        Provider provider = providerRepository.findProvider(providerId);
        if (provider == null) {
            return DataResponse.Failed("Provider not found!");
        }
        return DataResponse.Successful(provider);
    }
}
