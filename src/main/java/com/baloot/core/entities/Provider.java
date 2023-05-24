package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "providers")
public class Provider {
    @Id
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String registryDate;
    @Getter @Setter
    private double rating;
    @Column(length = 1000)
    @Getter @Setter
    private String image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider")
    private List<Commodity> commodities;

    public List<Commodity> getCommodities() {
        if (commodities == null)
            commodities = new ArrayList<>();
        return commodities;
    }

    public Provider(int id, String name, String registryDate, String image) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
        this.image = image;
        rating = 0;
    }

//    public double getRating() {
//        if (this.commodities.size() == 0)
//            return 0;
//        double res = 0;
//        for (var commodity: this.commodities) {
//            res += commodity.getRating();
//        }
//        return res / this.commodities.size();
//    }

    public void addCommodity(Commodity commodity){
        if (commodities == null)
            commodities = new ArrayList<>();
        rating *= commodities.size();
        rating += commodity.getRating();
        commodities.add(commodity);
        rating /= commodities.size();
    }
}
