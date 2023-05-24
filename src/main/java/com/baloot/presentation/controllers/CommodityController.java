package com.baloot.presentation.controllers;

import com.baloot.core.entities.CommodityRating;
import com.baloot.dataAccess.utils.QueryModel;
import com.baloot.presentation.models.AddCommentModel;
import com.baloot.presentation.models.AddRatingModel;
import com.baloot.presentation.utils.Container;
import com.baloot.responses.Response;
import com.baloot.service.CommodityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commodities")
public class CommodityController {
    @GetMapping
    public Response getCommodities(@ModelAttribute QueryModel query, HttpServletRequest request) {
        var service = Container.resolve(CommodityService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.getCommodities(query, username);
    }

    @GetMapping("/{id}")
    public Response getCommodity(@PathVariable int id, HttpServletRequest request) {
        var service = Container.resolve(CommodityService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.getCommodityById(id, username);
    }

    @GetMapping("/{id}/suggestions")
    public Response getCommoditySuggestions(@PathVariable int id, HttpServletRequest request) {
        var service = Container.resolve(CommodityService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.getSuggestions(id, username);
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
        var username = (String) request.getServletContext().getAttribute("username");
        return service.addComment(commentModel.text(), username, id);
    }

//    public Response voteComment()

    @PostMapping("/{id}/ratings")
    public Response addRating(@PathVariable int id, @RequestBody AddRatingModel ratingModel, HttpServletRequest request) {
        var service = Container.resolve(CommodityService.class);
        var username = (String) request.getServletContext().getAttribute("username");
        return service.rateCommodity(new CommodityRating(username, id, ratingModel.rate()));
    }
}
