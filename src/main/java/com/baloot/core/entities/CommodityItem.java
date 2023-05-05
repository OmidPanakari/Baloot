package com.baloot.core.entities;

import lombok.Getter;
import lombok.Setter;

public class CommodityItem {
    public CommodityItem(Commodity commodity, int inCart) {
        this.inCart = inCart;
        this.commodity = commodity;
    }
    @Getter @Setter
    private int inCart;
    @Getter
    private Commodity commodity;
}
