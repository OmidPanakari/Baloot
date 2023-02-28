package com.baloot;

import com.baloot.responses.Response;
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
                return userService.addUser(data);
            }
            case "addProvider" -> {
                return providerService.addProvider(data);
            }
            case "addCommodity" -> {
                return commodityService.addCommodity(data);
            }
            case "getCommoditiesList" -> {
                return commodityService.getCommodities();
            }
            case "rateCommodity" -> {
                return commodityService.rateCommodity(data);
            }
            case "addToBuyList" -> {
                return userService.addToBuyList(data);
            }
            case "removeFromBuyList" -> {
                return userService.removeFromBuyList(data);
            }
            case "getCommodityById" -> {
                return commodityService.getCommodityById(data);
            }
            case "getCommoditiesByCategory" -> {
                return commodityService.getCommoditiesByCategory(data);
            }
            case "getBuyList" -> {
                return userService.getBuyList(data);
            }
            default -> {
                return gson.toJson(new Response<>(false, "Not a valid command!"));
            }
        }
    }
}
