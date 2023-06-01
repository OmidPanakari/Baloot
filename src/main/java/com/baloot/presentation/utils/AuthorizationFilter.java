package com.baloot.presentation.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/auth/login") || request.getRequestURI().equals("/users/") || request.getRequestURI().equals("/users/oAuth"))
        {
            filterChain.doFilter(request, response);
            return;
        }
        var authHeader = request.getHeader("Authorization");
        if (TokenManager.isTokenValid(authHeader)) {
            var username = TokenManager.extractUsername(authHeader);
            request.getServletContext().setAttribute("username", username);
            filterChain.doFilter(request, response);
            return;
        }
        response.sendError(401);
    }
}
