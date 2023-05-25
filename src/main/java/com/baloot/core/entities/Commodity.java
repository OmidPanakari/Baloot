package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Table(name = "commodities")
public class Commodity {
    @Id
    @Getter @Setter
    private int id;
    @Getter
    private String name;
    @Getter
    private int price;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "categories", joinColumns = @JoinColumn(name = "commodityId"))
    @Getter
    private List<String> categories;
    @Getter
    private String image;
    @Transient
    private double rating;
    @Transient
    private int rateCount;
    @Getter @Setter
    private int inStock;
    @ManyToOne
    @JoinColumn(name = "providerId")
    @Getter @Setter
    private Provider provider;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commodity", fetch = FetchType.EAGER)
    @Getter
    private Set<CommodityRating> ratings;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commodity", fetch = FetchType.EAGER)
    @Getter
    private Set<Comment> comments;

    public Commodity(int id, String name, int price, int inStock, int providerId, List<String> categories, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        rating = 0;
        rateCount = 0;
        this.inStock = inStock;
        provider = new Provider();
        provider.setId(providerId);
        this.categories = new ArrayList<>(categories);
        this.image = image;
        ratings = new HashSet<>();
        comments = new HashSet<>();
    }

    public String getCategoryString() {
        return String.join(",", categories);
    }

    public void addRating(CommodityRating commodityRating) {
        if (ratings == null)
            ratings = new HashSet<>();
        ratings.add(commodityRating);
    }

    public double getRating() {
        if (ratings.size() == 0)
            return 0;
        return ratings.stream().mapToDouble(CommodityRating::getRating).sum() / ratings.size();
    }

    public int getRateCount() {
        return ratings.size();
    }
}
