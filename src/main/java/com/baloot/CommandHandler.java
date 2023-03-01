package com.baloot;

import com.baloot.entities.Commodity;
import com.baloot.entities.CommodityRate;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import com.baloot.models.CategoryModel;
import com.baloot.models.IdModel;
import com.baloot.models.UserCommodityModel;
import com.baloot.models.UsernameModel;
import com.baloot.responses.DataResponse;
import com.baloot.services.CommodityService;
import com.baloot.services.ProviderService;
import com.baloot.services.UserService;
import com.google.gson.Gson;

public class CommandHandler {

    private final UserService userService;
    private final ProviderService providerService;
    private final CommodityService commodityService;

    public CommandHandler(UserService userService, ProviderService providerService, CommodityService commodityService) {
        this.userService = userService;
        this.providerService = providerService;
        this.commodityService = commodityService;
    }

    public String executeCommand(String input) {
        var commandList = input.split(" ", 2);
        var command = commandList[0];
        Gson gson = new Gson();
        var data = commandList.length > 1 ? commandList[1] : "";
        switch (command) {
            case "addUser" -> {
                var user = new User(gson.fromJson(data, User.class));
                return gson.toJson(userService.addUser(user));
            }
            case "addProvider" -> {
                var provider = new Provider(gson.fromJson(data, Provider.class));
                return gson.toJson(providerService.addProvider(provider));
            }
            case "addCommodity" -> {
                var commodity = new Commodity(gson.fromJson(data, Commodity.class));
                return gson.toJson(commodityService.addCommodity(commodity));
            }
            case "getCommoditiesList" -> {
                return gson.toJson(commodityService.getCommodities());
            }
            case "rateCommodity" -> {
                var commodityRate = gson.fromJson(data, CommodityRate.class);
                return gson.toJson(commodityService.rateCommodity(commodityRate));
            }
            case "addToBuyList" -> {
                var userCommodityModel = gson.fromJson(data, UserCommodityModel.class);
                return gson.toJson(userService.addToBuyList(userCommodityModel));
            }
            case "removeFromBuyList" -> {
                var userCommodityModel = gson.fromJson(data, UserCommodityModel.class);
                return gson.toJson(userService.removeFromBuyList(userCommodityModel));
            }
            case "getCommodityById" -> {
                var idModel = gson.fromJson(data, IdModel.class);
                return gson.toJson(commodityService.getCommodityById(idModel));
            }
            case "getCommoditiesByCategory" -> {
                var categoryModel = gson.fromJson(data, CategoryModel.class);
                return gson.toJson(commodityService.getCommoditiesByCategory(categoryModel));
            }
            case "getBuyList" -> {
                var usernameModel = gson.fromJson(data, UsernameModel.class);
                return gson.toJson(userService.getBuyList(usernameModel));
            }
            default -> {
                return gson.toJson(new DataResponse<>(false, "Not a valid command!"));
            }
        }
    }
}
