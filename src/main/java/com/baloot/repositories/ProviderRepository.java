package com.baloot.repositories;

import com.baloot.entities.Provider;

import java.util.Objects;

public class ProviderRepository {
    private final Database database;
    public ProviderRepository(Database database) {
        this.database = database;
    }

    public void addProvider(Provider provider) {
        database.getProviders().add(provider);
    }

    public Provider findProvider(int id){
        for (Provider p: database.getProviders())
            if (Objects.equals(p.getId(), id))
                return p;
        return null;
    }
}
