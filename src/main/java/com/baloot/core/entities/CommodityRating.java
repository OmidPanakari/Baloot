package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "commodityRatings")
public class CommodityRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;
    @ManyToOne
    @JoinColumn(name = "username")
    @Getter
    private User user;
    @ManyToOne
    @JoinColumn(name = "commodityId")
    @Getter
    private Commodity commodity;
    @Getter
    private double rating;

    public CommodityRating(String username, int commodityId, double rating) {
        this.user = new User();
        this.user.setUsername(username);
        this.commodity = new Commodity();
        this.commodity.setId(commodityId);
        this.rating = rating;
    }
}
