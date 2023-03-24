package com.baloot.presentation.handlers;

import com.baloot.presentation.HtmlHelper;
import com.baloot.core.entities.User;
import com.baloot.responses.DataResponse;
import com.baloot.service.UserService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import java.io.File;

public class GetUserById implements Handler {
    private final UserService userService;

    public GetUserById(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var username = context.pathParam("username");
        var response = userService.getUser(username);
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        var user = ((DataResponse<User>)response).getData();
        var html = new File("src/main/static/User.html");
        var document = Jsoup.parse(html, "UTF-8");
        HtmlHelper.MakeUserElement(document, user);
        context.contentType("text/html");
        context.result(document.toString());
    }
}