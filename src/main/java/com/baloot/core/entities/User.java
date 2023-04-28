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
    private transient List<CommodityItem> buyList;
    private transient List<CommodityItem> purchased;

    public List<CommodityItem> getBuyList() {
        if (buyList == null)
            buyList = new ArrayList<>();
        return buyList;
    }

    public List<CommodityItem> getPurchased() {
        if (purchased == null)
            purchased = new ArrayList<>();
        return purchased;
    }

    public User(String password, String username, String email, String address, String birthDate, int credit){
        this.password = password;
        this.email = email;
        this.address = address;
        this.credit = credit;
        this.username = username;
        this.birthDate = birthDate;
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
        var ind = getBuyList().indexOf(getBuyList().stream().filter(c -> c.getCommodity().getId() == commodity.getId())
                .findFirst().orElse(null));
        if (ind == -1) {
            buyList.add(new CommodityItem(commodity, 1));
            return true;
        }
        buyList.get(ind).setCount(buyList.get(ind).getCount() + 1);
        return true;
    }

    public boolean removeFromBuyList(Commodity commodity){
        var ind = getBuyList().indexOf(getBuyList().stream().filter(c -> c.getCommodity().getId() == commodity.getId())
                .findFirst().orElse(null));
        if (ind == -1)
            return false;
        buyList.get(ind).setCount(buyList.get(ind).getCount() - 1);
        buyList.remove(ind);
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

    private void addToPurchaseList(List<CommodityItem> items) {
        for (var item : items) {
            var ind = getPurchased().indexOf(getPurchased().stream().filter(c ->
                            c.getCommodity().getId() == item.getCommodity().getId())
                    .findFirst().orElse(null));
            if (ind == -1)
                getPurchased().add(item);
            else
                getPurchased().get(ind).setCount(getPurchased().get(ind).getCount() + item.getCount());
        }
    }

}
