package com.baloot;

import com.baloot.handlers.*;
import com.baloot.services.CommodityService;
import com.baloot.services.ProviderService;
import com.baloot.services.UserService;
import io.javalin.Javalin;

import java.io.File;

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
        app.error(404, ctx -> {ctx.result(HtmlHelper.Get404Page()); ctx.contentType("text/html");});
        app.get("/commodities", new GetCommodities(commodityService));
        app.get("/commodities/{commodityId}", new GetCommodityById(commodityService));
        app.get("/providers/{providerId}", new GetProviderById(providerService));
        app.get("/users/{username}", new GetUserById(userService));
        app.get("/addCredit/{username}/{credit}", new AddCredit(userService));
        app.get("/addToBuyList/{username}/{commodityId}", new AddToBuyList(userService));
        app.get("/removeFromBuyList/{username}/{commodityId}", new RemoveFromBuyList(userService));
        app.get("/rateCommodity/{username}/{commodityId}/{rate}", new RateCommodity(commodityService));
        app.post("/rateCommodity/{username}/{commodityId}", new RateCommodityPost(commodityService));
        app.post("/voteComment/{commentId}", new VoteComment(userService));
        app.get("commodities/search/{startPrice}/{endPrice}", new SearchCommoditiesByPrice(commodityService));
        app.get("commodities/search/{category}", new SearchCommoditiesByCategory(commodityService));
    }
}
