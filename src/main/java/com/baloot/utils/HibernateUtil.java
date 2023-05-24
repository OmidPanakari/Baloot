package com.baloot.utils;

import com.baloot.core.entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final String HIBERNATE_CONFIG_FILE = "/hibernate.cfg.xml";

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure(HIBERNATE_CONFIG_FILE);

                // Add entity classes
                configuration.addAnnotatedClass(Commodity.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Provider.class);
                configuration.addAnnotatedClass(Comment.class);
                configuration.addAnnotatedClass(Discount.class);
                configuration.addAnnotatedClass(BuyListItem.class);
                configuration.addAnnotatedClass(PurchasedItem.class);
                configuration.addAnnotatedClass(CommodityRating.class);
                configuration.addAnnotatedClass(Vote.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
