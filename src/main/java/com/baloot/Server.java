package com.baloot;

import com.baloot.handlers.*;
import com.baloot.services.CommodityService;
import com.baloot.services.ProviderService;
import com.baloot.services.UserService;
import io.javalin.Javalin;

public class Server{

    private final UserService userService;
    private final ProviderService providerService;
    private final CommodityService commodityService;

    public Server(UserService userService, ProviderService providerService, CommodityService commodityService) {
        this.userService = userService;
        this.providerService = providerService;
        this.commodityService = commodityService;
    }
    public void startServer(){
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(7070);
        app.get("/commodities", new GetCommodities(commodityService));
        app.get("/commodities/{commodityId}", new GetCommodityById(commodityService));
        app.get("/providers/{providerId}", new GetProviderById());
        app.get("/users/{userId}", new GetUserById());
        app.get("/addCredit/{userId}/{credit}", new AddCredit());
        app.get("/addToBuyList/{username}/{commodityId}", new AddToBuyList());
        app.get("/removeFromBuyList/{username}/{commodityId}", new RemoveFromBuyList());
        app.get("/rateCommodity/{username}/{commodityId}/{rate}", new RateCommodity());
        app.get("/voteComment/{username}/{commentId}/{vote}", new VoteComment());
        app.get("commodities/search/{startPrice}/{endPrice}", new SearchCommoditiesByPrice());
        app.get("commodities/search/{category}", new SearchCommoditiesByCategory());
    }
}
