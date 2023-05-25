package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Provider;
import com.baloot.dataAccess.Database;
import com.baloot.utils.HibernateUtil;
import org.hibernate.Session;

import java.util.Objects;

public class ProviderRepository {
    private final Database database;
    public ProviderRepository(Database database) {
        this.database = database;
    }

    public Provider findProvider(int id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        var provider = session.get(Provider.class, id);
        session.close();
        return provider;
    }
}
