package com.baloot.presentation.controllers;

import com.baloot.presentation.Container;
import com.baloot.responses.DataResponse;
import com.baloot.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CreditServlet", value = "/credit")
public class CreditServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/credit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var service = Container.resolve(UserService.class);
        var session = req.getSession(false);
        String username = (String) session.getAttribute("username");
        int credit = Integer.parseInt(req.getParameter("credit"));
        var response = service.addCredit(username, credit);
        if (response.isSuccess()) {
            resp.sendRedirect("/buyList");
        }
        else {
            var message = ((DataResponse<String>)response).getData();
            req.setAttribute("message", message);
            req.getRequestDispatcher("/404.jsp").forward(req, resp);
        }
    }
}
