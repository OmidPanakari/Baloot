package com.baloot.presentation.handlers;

import com.baloot.presentation.HtmlHelper;
import com.baloot.core.entities.Commodity;
import com.baloot.responses.DataResponse;
import com.baloot.service.CommodityService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.List;

public class SearchCommoditiesByPrice implements Handler {
    private final CommodityService commodityService;

    public SearchCommoditiesByPrice(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var startPrice = Integer.parseInt(context.pathParam("startPrice"));
        var endPrice = Integer.parseInt(context.pathParam("endPrice"));
        var response = commodityService.getCommoditiesByPrice(startPrice, endPrice);
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        var commodities = ((DataResponse<List<Commodity>>)response).getData();
        var html = new File("src/main/static/Commodities.html");
        var document = Jsoup.parse(html, "UTF-8");
        var table = document.getElementsByTag("table");
        var data = HtmlHelper.GetCommoditiesTable(commodities);
        table.get(0).append(data);
        context.contentType("text/html");
        context.result(document.toString());
    }
}
