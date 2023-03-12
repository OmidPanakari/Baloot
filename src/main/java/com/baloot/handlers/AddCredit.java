package com.baloot.handlers;

import com.baloot.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AddCredit implements Handler {

    private final UserService userService;

    public AddCredit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var username = context.pathParam("username");
        var credit = Integer.parseInt(context.pathParam("credit"));
        var response = userService.addCredit(username, credit);
        if (!response.isSuccess())
            context.redirect("/forbidden");
        context.redirect("/users/" + username);
    }
}
