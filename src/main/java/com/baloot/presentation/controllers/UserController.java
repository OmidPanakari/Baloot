package com.baloot.presentation.controllers;

import com.baloot.presentation.models.AddCreditModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @PutMapping("/credit")
    public Response AddCredit(@RequestBody AddCreditModel creditModel, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var session = request.getSession(false);
        var username = (String) session.getAttribute("username");
        return service.addCredit(username, creditModel.credit());
    }
}
