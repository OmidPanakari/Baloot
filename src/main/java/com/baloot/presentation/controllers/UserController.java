package com.baloot.presentation.controllers;

import com.baloot.core.entities.User;
import com.baloot.presentation.models.AddCreditModel;
import com.baloot.presentation.models.UpdateCartModel;
import com.baloot.presentation.models.VoteModel;
import com.baloot.presentation.models.signupModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import com.baloot.service.models.UserCommodityModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = {"Authorization"})
@RestController
@RequestMapping("/users")
public class UserController {
    @PutMapping("/credit")
    public Response AddCredit(@RequestBody AddCreditModel creditModel, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.addCredit(username, creditModel.credit());
    }

    @GetMapping("/cart")
    public Response GetBuyList(HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.getBuyList(username);
    }

    @PutMapping("/cart")
    public Response UpdateBuyList(@RequestBody UpdateCartModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        if (Objects.equals(model.action(), "add")) {
            return service.addToBuyList(new UserCommodityModel(username, model.commodityId()));
        }
        else if (Objects.equals(model.action(), "remove")) {
            return service.removeFromBuyList(new UserCommodityModel(username, model.commodityId()));
        }
        else if (Objects.equals(model.action(), "buy")) {
            return service.purchaseBuyList(username, model.discount());
        }
        return DataResponse.Failed("Invalid action for request!");
    }

    @PostMapping("/votes")
    public Response voteCommodity(@RequestBody VoteModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.voteComment(username, model.commentId(), model.vote());
    }

    @PostMapping("/")
    public Response signup(@RequestBody signupModel model, HttpServletRequest request) {
        var service = Container.resolve(UserService.class);
        if (!Objects.equals(model.password(), model.confirmPassword()))
            return DataResponse.Failed("Passwords are not the same!");
        return service.addUser(new User(model.password(), model.username(), model.email(), model.address(),
                model.birthDate(), 0));
    }
}
