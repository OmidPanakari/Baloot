package com.baloot.presentation.handlers;

import com.baloot.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class PurchaseBuyList implements Handler {
    private final UserService userService;

    public PurchaseBuyList(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var username = context.pathParam("username");
        var response = userService.purchaseBuyList(username);
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        context.redirect("/users/" + username);
    }
}
