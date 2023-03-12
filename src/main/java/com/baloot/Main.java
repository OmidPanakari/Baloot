package com.baloot;

import com.baloot.repositories.CommodityRepository;
import com.baloot.repositories.Database;
import com.baloot.repositories.ProviderRepository;
import com.baloot.repositories.UserRepository;
import com.baloot.services.CommodityService;
import com.baloot.services.ProviderService;
import com.baloot.services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        var server = setup();
        server.startServer();
    }

    public static Server setup() {
        var database = new Database();

        var userRepository = new UserRepository(database);
        var providerRepository = new ProviderRepository(database);
        var commodityRepository = new CommodityRepository(database);

        var userService = new UserService(userRepository, commodityRepository);
        var providerService = new ProviderService(providerRepository);
        var commodityService = new CommodityService(providerRepository, commodityRepository, userRepository);

        return new Server(userService, providerService, commodityService);
    }
}