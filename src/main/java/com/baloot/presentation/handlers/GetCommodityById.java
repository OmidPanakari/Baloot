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

public class GetCommodityById implements Handler {

    private final CommodityService commodityService;

    public GetCommodityById(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        int commodityId = Integer.parseInt(context.pathParam("commodityId"));
        var username = context.queryParam("username");
        var response = commodityService.getCommodityById(commodityId);
        if (!response.isSuccess()) {
            context.status(403);
            return;
        }
        var commodity = ((DataResponse<Commodity>)response).getData();
        var html = new File("src/main/static/Commodity.html");
        var document = Jsoup.parse(html, "UTF-8");
        HtmlHelper.MakeCommodityElement(document, commodity);
        var commentsTable = HtmlHelper.GetCommentsTable(commodity.getComments(), username);
        document.getElementsByTag("table").get(0).append(commentsTable);
        if (username != null){
            document.getElementById("buy").attr("action", "/addToBuyList/"+username+"/"+commodityId);
            document.getElementById("buy").children().get(0).removeAttr("disabled");
            document.getElementById("rate").attr("action", "/rateCommodity/"+username+"/"+commodityId);
            document.getElementById("rate").children().get(2).removeAttr("disabled");
        }


        context.contentType("text/html");
        context.result(document.toString());
    }

}
