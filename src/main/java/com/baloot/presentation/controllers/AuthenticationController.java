package com.baloot.presentation.controllers;

import com.baloot.presentation.models.LoginModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public class AuthenticationController {
    @PostMapping("/login")
    public Response Login(@RequestBody LoginModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var response = service.login(model.getUsername(), model.getPassword());
        if (response.isSuccess()) {
            var session = request.getSession(true);
            session.setAttribute("username", model.getUsername());
        }
        return response;
    }

    @GetMapping("/logout")
    public Response Logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return DataResponse.Successful();
    }
}
