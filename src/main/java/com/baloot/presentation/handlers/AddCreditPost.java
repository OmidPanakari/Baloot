package com.baloot.presentation.handlers;

import com.baloot.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class AddCreditPost implements Handler {
    private final UserService userService;

    public AddCreditPost(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void handle(@NotNull Context context) throws Exception {
        var username = context.pathParam("username");
        var credit = Integer.parseInt(context.formParam("amount"));
        var response = userService.addCredit(username, credit);
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        context.redirect("/users/" + username);
    }
}
