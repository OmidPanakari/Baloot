package com.baloot.entities;
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
    @Getter
    private transient List<Commodity> buyList;

    public User(User user){
        password = user.password;
        email = user.email;
        address = user.address;
        credit = user.credit;
        username = user.username;
        birthDate = user.birthDate;
        buyList = new ArrayList<>();
    }

    public boolean addToBuyList(Commodity commodity){
        var ind = buyList.indexOf(commodity);
        if (ind != -1)
            return false;
        buyList.add(commodity);
        return true;
    }

    public boolean removeFromBuyList(Commodity commodity){
        var ind = buyList.indexOf(commodity);
        if (ind == -1)
            return false;
        buyList.remove(ind);
        return true;
    }

}
