package com.baloot.services;

import com.baloot.entities.User;
import com.baloot.models.UserCommodityModel;
import com.baloot.models.UsernameModel;
import com.baloot.repositories.CommodityRepository;
import com.baloot.repositories.UserRepository;
import com.baloot.responses.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private final UserRepository userRepository;
    private final CommodityRepository commodityRepository;
    private final Gson gson;

    public UserService(UserRepository userRepository, CommodityRepository commodityRepository) {
        this.userRepository = userRepository;
        this.commodityRepository = commodityRepository;
        gson = new GsonBuilder().create();
    }

    public String addUser(String data) {
        User user = new User(gson.fromJson(data, User.class));
        if (!isUserValid(user)){
            return gson.toJson(new Response<>(false, "User fields are not valid!"));
        }
        return gson.toJson(userRepository.addUser(user));
    }

    public String addToBuyList(String data) {
        var userCommodityModel = gson.fromJson(data, UserCommodityModel.class);
        var userToAdd = userRepository.findUser(userCommodityModel.getUsername());
        var commodityToAdd = commodityRepository.findCommodity(userCommodityModel.getCommodityId());
        if (commodityToAdd == null)
            return gson.toJson(new Response<>(false, "Commodity not found!"));
        if (userToAdd == null)
            return gson.toJson(new Response<>(false, "User not found!"));
        return gson.toJson(userToAdd.addToBuyList(commodityToAdd));
    }

    public String removeFromBuyList(String data) {
        var userCommodityModel = gson.fromJson(data, UserCommodityModel.class);
        var userToAdd = userRepository.findUser(userCommodityModel.getUsername());
        var commodityToAdd = commodityRepository.findCommodity(userCommodityModel.getCommodityId());
        if (commodityToAdd == null)
            return gson.toJson(new Response<>(false, "Commodity not found!"));
        if (userToAdd == null)
            return gson.toJson(new Response<>(false, "User not found!"));
        return gson.toJson(userToAdd.removeFromBuyList(commodityToAdd));
    }

    public String getBuyList(String data) {
        UsernameModel usernameModel = gson.fromJson(data, UsernameModel.class);
        User userToGetBuyList = userRepository.findUser(usernameModel.getUsername());
        if (userToGetBuyList == null)
            return gson.toJson(new Response<>(false, "User not found!"));
        return gson.toJson(new Response<>(true, userToGetBuyList.getBuyList()));
    }

    private boolean isUserValid(User user) {
        Pattern userNamePattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher userNameMatcher = userNamePattern.matcher(user.getUsername());
        return userNameMatcher.matches();
    }
}
