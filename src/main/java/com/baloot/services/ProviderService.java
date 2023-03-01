package com.baloot.services;

import com.baloot.entities.Provider;
import com.baloot.repositories.ProviderRepository;
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
}
