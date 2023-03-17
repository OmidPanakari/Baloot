package com.baloot;

import com.baloot.dataAccess.Database;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.presentation.Server;
import com.baloot.service.CommodityService;
import com.baloot.service.ProviderService;
import com.baloot.service.UserService;

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