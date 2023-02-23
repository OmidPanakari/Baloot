package com.baloot;

import com.baloot.entities.Commodity;
import com.baloot.entities.CommodityRate;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import com.baloot.models.CategoryModel;
import com.baloot.models.IdModel;
import com.baloot.models.UserCommodityModel;
import com.baloot.models.UsernameModel;
import com.baloot.repositories.CommodityRepository;
import com.baloot.repositories.ProviderRepository;
import com.baloot.repositories.UserRepository;
import com.baloot.responses.Response;
import com.google.gson.Gson;

public class CommandHandler {
    private UserRepository userRepository;
    private ProviderRepository providerRepository;
    private CommodityRepository commodityRepository;

    public CommandHandler() {
        this.userRepository = new UserRepository();
        this.providerRepository = new ProviderRepository();
        this.commodityRepository = new CommodityRepository();
    }

    public String executeCommand(String input) {
        var commandList = input.split(" ", 2);
        var command = commandList[0];
        Gson gson = new Gson();
        var data = commandList.length > 1 ? commandList[1] : "";
        switch (command) {
            case "addUser":
                User user = gson.fromJson(data, User.class);
                return gson.toJson(this.userRepository.addUser(user));
            case "addProvider":
                Provider provider = gson.fromJson(data, Provider.class);
                return gson.toJson(this.providerRepository.addProvider(provider));
            case "addCommodity":
                Commodity commodity = gson.fromJson(data, Commodity.class);
                var tempProvider = providerRepository.findProvider(commodity.getProviderId());
                if (tempProvider == null)
                    return gson.toJson(new Response<>(false, "Provider not found!"));
                return gson.toJson(this.commodityRepository.addCommodity(commodity));
            case "getCommoditiesList":
                return gson.toJson(this.commodityRepository.getCommodities());
            case "rateCommodity":
                CommodityRate commodityRate = gson.fromJson(data, CommodityRate.class);
                Commodity commodityToRate = commodityRepository.findCommodity(commodityRate.getCommodityId());
                if(commodityToRate == null)
                    return gson.toJson(new Response<>(false, "Commodity not found!"));
                if(userRepository.findUser(commodityRate.getUsername()) == null)
                    return gson.toJson(new Response<>(false, "User not found!"));
                commodityToRate.addRating(commodityRate);
                return gson.toJson(new Response<>(false, "Rate added."));
            case "addToBuyList", "removeFromBuyList":
                UserCommodityModel userCommodityModel = gson.fromJson(data, UserCommodityModel.class);
                User userToAdd = userRepository.findUser(userCommodityModel.username);
                Commodity commodityToAdd = commodityRepository.findCommodity(userCommodityModel.commodityId);
                if(commodityToAdd == null)
                    return gson.toJson(new Response<>(false, "Commodity not found!"));
                if(userToAdd == null)
                    return gson.toJson(new Response<>(false, "User not found!"));
                if (command.equals("addToBuyList"))
                    return gson.toJson(userToAdd.addToBuyList(commodityToAdd));
                else
                    return gson.toJson(userToAdd.addToBuyList(commodityToAdd));
            case "getCommodityById":
                IdModel idModel = gson.fromJson(data, IdModel.class);
                Commodity commodityToFind = commodityRepository.findCommodity(idModel.id);
                if(commodityToFind == null)
                    return gson.toJson(new Response<>(false, "Commodity not found!"));
                return gson.toJson(commodityToFind);
            case "getCommoditiesByCategory":
                CategoryModel categoryModel = gson.fromJson(data, CategoryModel.class);
                return gson.toJson(commodityRepository.getCommoditiesByCategory(categoryModel.category));
            case "getBuyList":
                UsernameModel usernameModel = gson.fromJson(data, UsernameModel.class);
                User userToGetBuyList = userRepository.findUser(usernameModel.username);
                if(userToGetBuyList == null)
                    return gson.toJson(new Response<>(false, "User not found!"));
                return gson.toJson(userToGetBuyList.getBuyList());
            default:
                return gson.toJson(new Response<>(false, "Not a valid command!"));

        }
    }
}
