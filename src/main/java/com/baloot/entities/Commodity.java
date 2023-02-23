package com.baloot.entities;

import lombok.Getter;
import lombok.Setter;

public class Commodity {
    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int price;
    @Getter @Setter
    private String categories;
    @Getter @Setter
    private double rating;
    @Getter @Setter
    private int inStock;
}
