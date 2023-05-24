package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @ElementCollection
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commodity")
    private List<CommodityRating> ratings;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commodity")
    private List<Comment> comments;

    public List<CommodityRating> getRatings() {
        if (ratings == null)
            ratings = new ArrayList<>();
        return ratings;
    }

    public List<Comment> getComments() {
        if (comments == null)
            comments = new ArrayList<>();
        return comments;
    }

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
        ratings = new ArrayList<>();
        comments = new ArrayList<>();
    }
    public double getRating() {
        if (ratings == null)
            ratings = new ArrayList<>();
        if (ratings.size() == 0)
            return 0;
        double res = 0;
        for (var rating : ratings) {
            res += rating.getRating();
        }
        return res / ratings.size();
    }

    public String getCategoryString() {
        return String.join(",", categories);
    }

    public void addRating(CommodityRating commodityRate) {
        for (int i = 0; i < ratings.size(); i++) {
            if (Objects.equals(ratings.get(i).getUser().getUsername(), commodityRate.getUser().getUsername())) {
                ratings.set(i, commodityRate);
                return;
            }
        }
        ratings.add(commodityRate);
        rateCount++;
    }
    public void addComment(Comment comment){
        if (comments == null)
            comments = new ArrayList<>();
        comments.add(comment);
    }
    public boolean isInList(String category){
        return categories.stream().anyMatch(c -> c.toLowerCase().contains(category.toLowerCase()));
    }
}
