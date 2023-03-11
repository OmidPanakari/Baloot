package com.baloot;

import com.baloot.handlers.*;
import io.javalin.Javalin;

public class Server{
    public void startServer(){
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(7070);
        app.get("/commodities", new GetCommodities());
        app.get("/commodities/{commodityId}", new GetCommodities());
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
