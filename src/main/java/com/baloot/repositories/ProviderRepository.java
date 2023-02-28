package com.baloot.repositories;

import com.baloot.entities.Provider;
import com.baloot.responses.Response;

import java.util.Objects;

public class ProviderRepository {
    private final Database database;
    public ProviderRepository(Database database) {
        this.database = database;
    }

    public Response<String> addProvider(Provider provider){
        Provider existingProvider = findProvider(provider.getId());
        if (existingProvider != null)
            return new Response<>(false, "Provider id is taken!");
        database.getProviders().add(provider);
        return new Response<>(true, "Provider added.");
    }

    public Provider findProvider(int id){
        for (Provider p: database.getProviders())
            if (Objects.equals(p.getId(), id))
                return p;
        return null;
    }
}
