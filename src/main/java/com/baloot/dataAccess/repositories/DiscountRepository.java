package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Discount;
import com.baloot.dataAccess.Database;
import com.baloot.utils.HibernateUtil;
import org.hibernate.Session;

public class DiscountRepository {
    private final Database database;

    public DiscountRepository(Database database) {
        this.database = database;
    }

    public Discount getDiscount(String discountCode) {
        if (discountCode == null)
            return null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        var discount  = session.get(Discount.class, discountCode);
        session.close();
        return discount;
    }
}
