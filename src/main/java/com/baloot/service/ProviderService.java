package com.baloot.service;

import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.Provider;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.presentation.models.ProviderModel;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.models.CommodityModel;

import java.util.stream.Collectors;

public class ProviderService {
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    public ProviderService(ProviderRepository providerRepository, UserRepository userRepository) {
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    public Response addProvider(Provider provider) {
        if (providerRepository.findProvider(provider.getId()) != null)
            return DataResponse.Failed("Provider id is taken!");
        providerRepository.addProvider(provider);
        return DataResponse.Successful();
    }

    public Response getProviderById(int providerId, String username) {
        Provider provider = providerRepository.findProvider(providerId);
        var user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        if (provider == null) {
            return DataResponse.Failed("Provider not found!");
        }
        return DataResponse.Successful(convertToModel(provider, user));
    }

    private CommodityModel convertToModel(Commodity commodity, User user) {
        var item = user.getBuyList().stream()
            .filter(c -> c.getCommodity().getId() == commodity.getId())
            .findFirst()
            .orElse(null);
        int inCart = (item == null) ? 0 : item.getInCart();
        return new CommodityModel(commodity, inCart);
    }

    private ProviderModel convertToModel(Provider provider, User user) {
        var commodities = provider.getCommodities().stream().map(c -> convertToModel(c, user))
            .collect(Collectors.toList());
        return new ProviderModel(provider, commodities);
    }
}
