package com.baloot.service;

import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.Provider;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.presentation.models.ProviderModel;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.models.CommodityItemModel;
import com.baloot.service.models.CommodityModel;
import com.baloot.service.models.Converter;

import java.util.stream.Collectors;

public class ProviderService {
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    public ProviderService(ProviderRepository providerRepository, UserRepository userRepository) {
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    public Response getProviderById(int providerId, String username) {
        Provider provider = providerRepository.findProvider(providerId);
        var user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        if (provider == null) {
            return DataResponse.Failed("Provider not found!");
        }
        return DataResponse.Successful(Converter.convertToModel(provider, user));
    }
}
