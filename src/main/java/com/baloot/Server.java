package com.baloot;

import com.baloot.handlers.GetCommodities;
import io.javalin.Javalin;

public class Server{
    public void startServer(){
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(7070);
        app.get("/commodities", new GetCommodities());
        app.get("/commodities/{commodity_id}", new GetCommodities());
    }
}
