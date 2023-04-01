package com.baloot.presentation;

import com.baloot.presentation.controllers.CommodityServlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(servletNames = {"CommoditiesServlet", "HomeServlet", "CommodityServlet", "CreditServlet", "BuyListServlet"})
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;
        var session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("/login");
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
