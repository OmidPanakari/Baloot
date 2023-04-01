package com.baloot.presentation;

import com.baloot.dataAccess.Database;
import com.baloot.dataAccess.repositories.*;
import com.baloot.service.CommodityService;
import com.baloot.service.ProviderService;
import com.baloot.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.io.IOException;

public class StartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Container.register(Database.class);

        Container.register(CommentRepository.class);
        Container.register(CommodityRepository.class);
        Container.register(ProviderRepository.class);
        Container.register(UserRepository.class);
        Container.register(DiscountRepository.class);

        Container.register(CommodityService.class);
        Container.register(ProviderService.class);
        Container.register(UserService.class);

        Container.resolve(Database.class).init();
    }
}
