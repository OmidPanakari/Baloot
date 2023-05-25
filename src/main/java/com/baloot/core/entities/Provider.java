package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    @Transient
    private double rating;
    @Column(length = 1000)
    @Getter @Setter
    private String image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider", fetch = FetchType.EAGER)
    @Getter
    private Set<Commodity> commodities;

    public Provider(int id, String name, String registryDate, String image) {
        this.id = id;
        this.name = name;
        this.registryDate = registryDate;
        this.image = image;
        rating = 0;
    }

    public double getRating() {
        return commodities.stream().mapToDouble(Commodity::getRating).sum() / commodities.size();
    }
}
