package com.baloot.handlers;

import com.baloot.entities.CommodityRate;
import com.baloot.services.CommodityService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class RateCommodity implements Handler {
    private final CommodityService commodityService;

    public RateCommodity(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var username = context.pathParam("username");
        var commodityId = Integer.parseInt(context.pathParam("commodityId"));
        var rate = Double.parseDouble(context.pathParam("rate"));
        var response = commodityService.rateCommodity(new CommodityRate(username, commodityId, rate));
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        context.redirect("/commodities/" + commodityId);
    }
}
