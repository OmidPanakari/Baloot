package com.baloot.core.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Column(unique = true)
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String birthDate;
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int credit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    @Getter
    private Set<BuyListItem> buyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    @Getter
    private Set<PurchasedItem> purchased;

    public User(String password, String username, String email, String address, String birthDate, int credit){
        this.password = password;
        this.email = email;
        this.address = address;
        this.credit = credit;
        this.username = username;
        this.birthDate = birthDate;
        buyList = new HashSet<>();
        purchased = new HashSet<>();
    }

    public User(User user){
        password = user.password;
        email = user.email;
        address = user.address;
        credit = user.credit;
        username = user.username;
        birthDate = user.birthDate;
        buyList = new HashSet<>();
        purchased = new HashSet<>();
    }

    public boolean addToBuyList(Commodity commodity){
        var item = getBuyList().stream().filter(c -> c.getCommodity().getId() == commodity.getId())
                .findFirst().orElse(null);
        if (item == null) {
            buyList.add(new BuyListItem(commodity, 1));
            return true;
        }
        item.setInCart(item.getInCart() + 1);
        return true;
    }

    public boolean removeFromBuyList(Commodity commodity){
        var item = getBuyList().stream().filter(c -> c.getCommodity().getId() == commodity.getId())
                .findFirst().orElse(null);
        if (item == null)
            return false;
        item.setInCart(item.getInCart() - 1);
        if (item.getInCart() == 0) {
            buyList.remove(item);
        }
        return true;
    }

    public boolean purchaseBuyList(Discount discount) {
        var price = getBuyList().stream().mapToInt(c -> c.getCommodity().getPrice()).sum();
        if (discount != null) {
            price = price * (100 - discount.getDiscount()) / 100;
        }
        if (credit < price)
            return false;
        credit -= price;
        addToPurchaseList(buyList);
        buyList.clear();
        return true;
    }

    private void addToPurchaseList(Set<BuyListItem> items) {
        for (var item : items) {
            var purchasedItem = getPurchased().stream().filter(c ->
                    c.getCommodity().getId() == item.getCommodity().getId())
                    .findFirst().orElse(null);
            if (purchasedItem == null)
                getPurchased().add(new PurchasedItem(item.getCommodity(), item.getInCart()));
            else
                purchasedItem.setInCart(purchasedItem.getInCart() + item.getInCart());
        }
    }

}
