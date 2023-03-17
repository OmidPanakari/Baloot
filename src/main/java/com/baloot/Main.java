package com.baloot;

import com.baloot.repositories.*;
import com.baloot.services.CommodityService;
import com.baloot.services.ProviderService;
import com.baloot.services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        var server = setup();
        server.startServer();
    }

    public static Server setup() throws Exception {
        var database = new Database();
        database.init();

        var userRepository = new UserRepository(database);
        var providerRepository = new ProviderRepository(database);
        var commodityRepository = new CommodityRepository(database);
        var commentRepository = new CommentRepository(database);

        var userService = new UserService(userRepository, commodityRepository, commentRepository);
        var providerService = new ProviderService(providerRepository);
        var commodityService = new CommodityService(providerRepository, commodityRepository, userRepository);

        return new Server(userService, providerService, commodityService);
    }
}