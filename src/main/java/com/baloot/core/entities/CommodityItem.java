package com.baloot.core.entities;

import lombok.Getter;
import lombok.Setter;

public class CommodityItem {
    public CommodityItem(Commodity commodity, int count) {
        this.count = count;
        this.commodity = commodity;
    }
    @Getter @Setter
    private int count;
    @Getter
    private Commodity commodity;
}
