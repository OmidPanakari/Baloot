package com.baloot.presentation.controllers;

import com.baloot.core.entities.Commodity;
import com.baloot.presentation.Container;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.CommodityService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@WebServlet(name="CommoditiesServlet", value = "/commodities")
public class CommoditiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var service = Container.resolve(CommodityService.class);
        var action = req.getParameter("action");
        if (Objects.equals(action, "clear")) {
            resp.sendRedirect("/commodities");
            return;
        }
        var search = req.getParameter("search");
        var searchType = req.getParameter("searchType");
        var sort = req.getParameter("sort");
        Response response;
        if (search != null) {
            if (searchType.equals("category")) {
                response = service.getCommoditiesByCategory(search);
            }
            else {
                response = service.getCommoditiesByName(search);
            }
        }
        else {
            response = service.getCommodities();
        }
        if (response.isSuccess()) {
            var commodities = ((DataResponse<List<Commodity>>)response).getData();
            if (Objects.equals(sort, "rate")) {
                Comparator<Commodity> byRating = Comparator.comparingDouble(Commodity::getRating);
                commodities.sort(byRating);
            }
            req.setAttribute("commodities", commodities);
            req.getRequestDispatcher("/commodities.jsp").forward(req, resp);
        }
        else {
            var message = ((DataResponse<String>)response).getData();
            req.setAttribute("message", message);
            req.getRequestDispatcher("/400.jsp").forward(req, resp);
        }
    }
}
