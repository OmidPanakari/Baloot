package com.baloot.handlers;

import com.baloot.entities.Commodity;
import com.baloot.responses.DataResponse;
import com.baloot.services.CommodityService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import javax.lang.model.element.Element;
import java.io.File;
import java.util.List;

public class GetCommodities implements Handler {

    private final CommodityService commodityService;

    public GetCommodities(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var response = commodityService.getCommodities();
        if (!response.isSuccess())
            context.redirect("/forbidden");
        var commodities = ((DataResponse<List<Commodity>>)response).getData();
        var html = new File("src/main/static/Commodities.html");
        var document = Jsoup.parse(html, "UTF-8");
        var table = document.getElementsByTag("table");
        var data = GetCommoditiesTable(commodities);
        table.get(0).append(data);
        context.contentType("text/html");
        context.result(document.toString());
    }

    private String GetCommoditiesTable(List<Commodity> commodities) {
        String result = "";
        for (var commodity : commodities) {
            result += GetCommodityRow(commodity);
        }
        return result;
    }

    private String GetCommodityRow(Commodity commodity) {
        return  "<tr>" +
                "<td>" + commodity.getId() + "</td>" +
                "<td>" + commodity.getName() + "</td>" +
                "<td>" + commodity.getProviderId() + "</td>" +
                "<td>" + commodity.getPrice() + "</td>" +
                "<td>" + String.join(",", commodity.getCategories()) + "</td>" +
                "<td>" + commodity.getRating() + "</td>" +
                "<td>" + commodity.getInStock() + "</td>" +
                "<td>" + "<a href=\"/commodities/" + commodity.getId() + "\">Info</a>" + "</td>" +
                "</tr>";
    }
}
