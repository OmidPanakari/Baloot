package com.baloot.presentation.controllers;

import com.baloot.presentation.models.AddCreditModel;
import com.baloot.presentation.models.UpdateCartModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import com.baloot.service.models.UserCommodityModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/cart")
    public Response GetBuyList(HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var session = request.getSession(false);
        var username = (String) session.getAttribute("username");
        return service.getBuyList(username);
    }

    @PutMapping("/cart")
    public Response UpdateBuyList(@RequestBody UpdateCartModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var session = request.getSession(false);
        var username = (String) session.getAttribute("username");
        if (Objects.equals(model.action(), "add")) {
            return service.addToBuyList(new UserCommodityModel(username, model.commodityId()));
        }
        else if (Objects.equals(model.action(), "remove")) {
            return service.removeFromBuyList(new UserCommodityModel(username, model.commodityId()));
        }
        return DataResponse.Failed("Invalid action for request!");
    }
}
