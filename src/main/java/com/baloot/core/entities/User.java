package com.baloot.core.entities;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class User {
    @Getter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String birthDate;
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int credit;
    private transient List<Commodity> buyList;
    private transient List<Commodity> purchased;

    public List<Commodity> getBuyList() {
        if (buyList == null)
            buyList = new ArrayList<>();
        return buyList;
    }

    public List<Commodity> getPurchased() {
        if (purchased == null)
            purchased = new ArrayList<>();
        return purchased;
    }

    public User(String password, String username, String email, String address, String birthDate, int credit){
        password = password;
        email = email;
        address = address;
        credit = credit;
        username = username;
        birthDate = birthDate;
        buyList = new ArrayList<>();
        purchased = new ArrayList<>();
    }

    public User(User user){
        password = user.password;
        email = user.email;
        address = user.address;
        credit = user.credit;
        username = user.username;
        birthDate = user.birthDate;
        buyList = new ArrayList<>();
        purchased = new ArrayList<>();
    }

    public boolean addToBuyList(Commodity commodity){
        var ind = getBuyList().indexOf(commodity);
        if (ind != -1)
            return false;
        buyList.add(commodity);
        return true;
    }

    public boolean removeFromBuyList(Commodity commodity){
        var ind = getBuyList().indexOf(commodity);
        if (ind == -1)
            return false;
        buyList.remove(ind);
        return true;
    }

    public boolean purchaseBuyList(Discount discount) {
        var price = getBuyList().stream().mapToInt(Commodity::getPrice).sum();
        if (discount != null) {
            price = price * (100 - discount.getDiscount()) / 100;
        }
        if (credit < price)
            return false;
        credit -= price;
        getPurchased().addAll(buyList);
        buyList.clear();
        return true;
    }

}
