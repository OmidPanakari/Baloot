package com.baloot.presentation.controllers;

import com.baloot.core.entities.CommodityRate;
import com.baloot.dataAccess.utils.QueryModel;
import com.baloot.presentation.models.AddCommentModel;
import com.baloot.presentation.models.AddRatingModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.Response;
import com.baloot.service.CommodityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/commodities")
public class CommodityController {
    @GetMapping
    public Response getCommodities(@ModelAttribute QueryModel query, HttpSession session) {
        int a = (int)session.getAttribute("a");
        var service = Container.resolve(CommodityService.class);
        return service.getCommodities(query);
    }

    @GetMapping("/{id}")
    public Response getCommodity(@PathVariable int id) {
        var service = Container.resolve(CommodityService.class);
        return service.getCommodityById(id);
    }

    @GetMapping("/{id}/suggestions")
    public Response getCommoditySuggestions(@PathVariable int id) {
        var service = Container.resolve(CommodityService.class);
        return service.getSuggestions(id);
    }

    @GetMapping("/{id}/comments")
    public Response getCommodityComments(@PathVariable int id, HttpSession session) {
        session.setAttribute("a", 1);
        var service = Container.resolve(CommodityService.class);
        return service.getComments(id);
    }

    @PostMapping("/{id}/comments")
    public Response addComment(@PathVariable int id, @RequestBody AddCommentModel commentModel, HttpServletRequest request) {
        var service = Container.resolve(CommodityService.class);
        var session = request.getSession(false);
        var username = (String) session.getAttribute("username");
        return service.addComment(commentModel.text(), username, id);
    }

//    public Response voteComment()

    @PostMapping("/{id}/ratings")
    public Response addRating(@PathVariable int id, @RequestBody AddRatingModel ratingModel, HttpServletRequest request) {
        var service = Container.resolve(CommodityService.class);
        var session = request.getSession(false);
        var username = (String) session.getAttribute("username");
        return service.rateCommodity(new CommodityRate(username, id, ratingModel.rate()));
    }
}
