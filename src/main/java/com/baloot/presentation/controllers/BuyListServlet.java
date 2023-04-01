package com.baloot.presentation.controllers;

import com.baloot.core.entities.User;
import com.baloot.presentation.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import com.baloot.service.models.UserCommodityModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.crypto.Data;
import java.io.IOException;

@WebServlet(name = "BuyListServlet", value = "/buyList")
public class BuyListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(false);
        String username = (String) session.getAttribute("username");
        var service = Container.resolve(UserService.class);
        var response = service.getUser(username);
        if (response.isSuccess()) {
            var user = ((DataResponse<User>)response).getData();
            req.setAttribute("user", user);
            req.getRequestDispatcher("/buyList.jsp").forward(req, resp);
        }
        else {
            var message = ((DataResponse<String>)response).getData();
            req.setAttribute("message", message);
            req.getRequestDispatcher("/400.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var action = req.getParameter("action");
        var service = Container.resolve(UserService.class);
        var session = req.getSession(false);
        var username = (String) session.getAttribute("username");
        var discountCode = req.getParameter("discount");
        Response response;
        switch (action) {
            case "pay":
                response = service.purchaseBuyList(username, discountCode);
                break;
            case "remove":
                int commodityId = Integer.parseInt(req.getParameter("commodityId"));
                response = service.removeFromBuyList(new UserCommodityModel(username, commodityId));
                break;
            default:
                response = new DataResponse<>(false, "Invalid action!");
        }
        if (response.isSuccess()) {
            resp.sendRedirect("/buyList");
        }
        else {
            var message = ((DataResponse<String>)response).getData();
            req.setAttribute("message", message);
            req.getRequestDispatcher("/400.jsp").forward(req, resp);
        }
    }
}
