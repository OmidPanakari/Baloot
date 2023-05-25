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
    @Getter
    private double rating;
    @Getter
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

    public void updateRating(CommodityRating commodityRate) {
        rateCount = ratings.size();
        rating = ratings.stream().mapToDouble(CommodityRating::getRating).sum() / rateCount;
    }
    public boolean isInList(String category){
        return categories.stream().anyMatch(c -> c.toLowerCase().contains(category.toLowerCase()));
    }
}
