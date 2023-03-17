package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Provider;
import com.baloot.dataAccess.Database;

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
