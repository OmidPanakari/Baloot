package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "purchasedItems")
public class PurchasedItem {
    public PurchasedItem(Commodity commodity, int inCart) {
        this.inCart = inCart;
        this.commodity = commodity;
    }
    @Id
    @Getter
    private int id;
    @Getter @Setter
    private int inCart;
    @ManyToOne
    @JoinColumn(name = "commodityId")
    @Getter
    private Commodity commodity;
    @ManyToOne
    @JoinColumn(name = "username")
    @Getter
    private User user;
}
