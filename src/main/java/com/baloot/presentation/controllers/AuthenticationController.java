package com.baloot.presentation.controllers;

import com.baloot.dataAccess.utils.HashHelper;
import com.baloot.presentation.models.LoginModel;
import com.baloot.presentation.utils.Container;
import com.baloot.presentation.utils.TokenManager;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @PostMapping("/login")
    public Response login(@RequestBody LoginModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var response = service.login(model.username(), HashHelper.DoubleSha256(model.password()));
        if (response.isSuccess()) {
            var token = TokenManager.generateToken(model.username());
            return DataResponse.Successful(token);
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
        var username = (String) request.getServletContext().getAttribute("username");
        return DataResponse.Successful(service.getUser(username));
    }
}
