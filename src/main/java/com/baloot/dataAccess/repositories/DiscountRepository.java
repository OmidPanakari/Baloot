package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Discount;
import com.baloot.dataAccess.Database;

public class DiscountRepository {
    private final Database database;

    public DiscountRepository(Database database) {
        this.database = database;
    }

    public Discount getDiscount(String discountCode) {
        for (Discount discount : database.getDiscounts()) {
            if (discount.getDiscountCode().equals(discountCode)) {
                return discount;
            }
        }
        return null;
    }
}
