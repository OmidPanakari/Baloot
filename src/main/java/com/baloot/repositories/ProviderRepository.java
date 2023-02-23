package com.baloot.repositories;

import com.baloot.entities.Provider;
import com.baloot.entities.User;
import com.baloot.responses.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProviderRepository {

    private final List<Provider> providers;
    public ProviderRepository() {
        providers = new ArrayList<>();
    }

    public Response<String> addProvider(Provider provider){
        Provider existingProvider = findProvider(provider.getId());
        if (existingProvider == null)
            return new Response<>(false, "Provider id is taken!");
        providers.add(provider);
        return new Response<>(true, "Provider added.");
    }

    public Provider findProvider(int id){
        for (Provider p: providers)
            if (Objects.equals(p.getId(), id))
                return p;
        return null;
    }
}
