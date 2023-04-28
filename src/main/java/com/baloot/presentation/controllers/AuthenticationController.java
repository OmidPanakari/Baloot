package com.baloot.presentation.controllers;

import com.baloot.presentation.models.LoginModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @PostMapping("/login")
    public Response login(@RequestBody LoginModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var response = service.login(model.username(), model.password());
        if (response.isSuccess()) {
            var session = request.getSession(true);
            session.setAttribute("username", model.username());
        }
        return response;
    }

    @GetMapping("/logout")
    public Response logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return DataResponse.Successful();
    }

    @GetMapping("/")
    public Response isLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var service = Container.resolve(UserService.class);
        var session = request.getSession(false);
        if (session == null)
            return DataResponse.Successful(false);
        var username = (String) session.getAttribute("username");
        return DataResponse.Successful(username != null);
    }
}
