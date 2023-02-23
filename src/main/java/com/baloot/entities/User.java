package com.baloot.entities;
import com.baloot.responses.Response;
import lombok.Getter;
import lombok.Setter;

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
    private transient BuyList buyList;

    public User(User user){
        password = user.password;
        email = user.email;
        address = user.address;
        credit = user.credit;
        username = user.username;
        birthDate = user.birthDate;
        buyList = new BuyList();
    }

    public Response<String> addToBuyList(Commodity commodity){
        return buyList.addToBuyList(commodity);
    }

    public Response<String> removeFromBuyList(Commodity commodity){
        return buyList.removeFromBuyList(commodity);
    }

}
