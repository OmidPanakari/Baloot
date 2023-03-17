package com.baloot.models;

import lombok.Getter;

public class UsernameModel {

    public UsernameModel(String username) {
        this.username = username;
    }
    @Getter
    private String username;
}
