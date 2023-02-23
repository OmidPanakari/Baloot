package com.baloot.entities;
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
    private String credit;
    @Getter
    private BuyList buyList;

    public User(String username, String password, String birthDate, String email, String address, String credit){
        this.password = password;
        this.email = email;
        this.address = address;
        this.credit = credit;
        this.username = username;
        this.birthDate = birthDate;
        buyList = new BuyList();
    }

}
