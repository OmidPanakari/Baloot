package com.baloot;

import com.baloot.dataAccess.Database;
import com.baloot.dataAccess.repositories.*;
import com.baloot.presentation.utils.Container;
import com.baloot.service.CommodityService;
import com.baloot.service.DiscountService;
import com.baloot.service.ProviderService;
import com.baloot.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Container.register(Database.class);

        Container.register(CommentRepository.class);
        Container.register(CommodityRepository.class);
        Container.register(ProviderRepository.class);
        Container.register(UserRepository.class);
        Container.register(DiscountRepository.class);

        Container.register(CommodityService.class);
        Container.register(ProviderService.class);
        Container.register(UserService.class);
        Container.register(DiscountService.class);

        Container.resolve(Database.class).init();
        SpringApplication.run(Main.class, args);
    }
}

