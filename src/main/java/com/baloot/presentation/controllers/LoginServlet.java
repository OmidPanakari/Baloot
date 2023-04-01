package com.baloot.presentation.controllers;

import com.baloot.presentation.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            resp.sendRedirect("/");
        }
        else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var username = req.getParameter("username");
        var password = req.getParameter("password");
        var service = Container.resolve(UserService.class);
        var response = service.login(username, password);
        if (response.isSuccess()) {
            var session = req.getSession(true);
            session.setAttribute("username", username);
            resp.sendRedirect("/");
        }
        else {
            req.setAttribute("message", ((DataResponse<String>)response).getData());
            req.getRequestDispatcher("/400.jsp").forward(req, resp);
        }
    }
}
