package com.baloot.handlers;

import com.baloot.models.UserCommodityModel;
import com.baloot.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class RemoveFromBuyList implements Handler {
    private final UserService userService;

    public RemoveFromBuyList(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var username = context.pathParam("username");
        var commodityId = Integer.parseInt(context.pathParam("commodityId"));
        var response = userService.removeFromBuyList(new UserCommodityModel(username, commodityId));
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        context.redirect("/users/" + username);
    }
}
