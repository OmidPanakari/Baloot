package com.baloot.handlers;

import com.baloot.services.CommodityService;
import com.baloot.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoteComment implements Handler {
    private final UserService userService;

    public VoteComment(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var commentId = Integer.parseInt(context.pathParam("commentId"));
        var vote = Integer.parseInt(Objects.requireNonNull(context.formParam("vote")));
        var username = context.formParam("username");
        userService.voteComment(username, commentId, vote);
    }
}
