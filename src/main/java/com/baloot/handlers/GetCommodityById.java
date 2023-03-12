package com.baloot.handlers;

import com.baloot.HtmlHelper;
import com.baloot.entities.Commodity;
import com.baloot.responses.DataResponse;
import com.baloot.services.CommodityService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class GetCommodityById implements Handler {

    private final CommodityService commodityService;

    public GetCommodityById(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int commodityId = Integer.parseInt(context.pathParam("commodityId"));
        var response = commodityService.getCommodityById(commodityId);
        if (!response.isSuccess())
            context.redirect("/forbidden");
        var commodity = ((DataResponse<Commodity>)response).getData();
        var html = new File("src/main/static/Commodity.html");
        var document = Jsoup.parse(html, "UTF-8");
        HtmlHelper.MakeCommodityElement(document, commodity);
        context.contentType("text/html");
        context.result(document.toString());
    }

}
