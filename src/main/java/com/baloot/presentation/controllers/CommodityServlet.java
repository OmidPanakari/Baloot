package com.baloot.presentation.controllers;

import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityRate;
import com.baloot.presentation.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.CommodityService;
import com.baloot.service.UserService;
import com.baloot.service.models.UserCommodityModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CommodityServlet", value = "/commodities/*")
public class CommodityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathParts = req.getPathInfo().split("/");
        var commodityId = Integer.parseInt(pathParts[1]);
        var service = Container.resolve(CommodityService.class);
        var response = service.getCommodityById(commodityId);
        if (response.isSuccess()) {
            var commodity = ((DataResponse<Commodity>) response).getData();
            req.setAttribute("commodity", commodity);
            req.setAttribute("comments", commodity.getComments());
            req.getRequestDispatcher("/commodity.jsp").forward(req, resp);
        }
        else {
            var message = ((DataResponse<String>) response).getData();
            req.setAttribute("message", message);
            req.getRequestDispatcher("/400.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var pathParts = req.getPathInfo().split("/");
        var commodityId = Integer.parseInt(pathParts[1]);
        String username = (String) req.getSession(false).getAttribute("username");
        String action = req.getParameter("action");
        var commodityService = Container.resolve(CommodityService.class);
        var userService = Container.resolve(UserService.class);
        Response response;
        switch (action) {
            case "comment":
                String text = req.getParameter("comment");
                response = commodityService.addComment(text, username, commodityId);
                break;
            case "add":
                response = userService.addToBuyList(new UserCommodityModel(username, commodityId));
                break;
            case "rate":
                int rate = Integer.parseInt(req.getParameter("quantity"));
                response = commodityService.rateCommodity(new CommodityRate(username, commodityId, rate));
                break;
            case "like","dislike":
                int commentId = Integer.parseInt(req.getParameter("commentId"));
                response = userService.voteComment(username, commentId, action.equals("like") ? 1:-1);
                break;
            default:
                response = new DataResponse<>(false, "Invalid action!");
        }
        if (response.isSuccess()) {
            resp.sendRedirect("/commodities/" + commodityId);
        }
        else {
            var message = ((DataResponse<String>)response).getData();
            req.setAttribute("message", message);
            req.getRequestDispatcher("/400.jsp").forward(req, resp);
        }

    }
}
