package com.baloot.handlers;

import com.baloot.HtmlHelper;
import com.baloot.entities.Provider;
import com.baloot.responses.DataResponse;
import com.baloot.services.ProviderService;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;

import java.io.File;

public class GetProviderById implements Handler {
    private final ProviderService providerService;

    public GetProviderById(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        var providerId = Integer.parseInt(context.pathParam("providerId"));
        var response = providerService.getProviderById(providerId);
        if (!response.isSuccess())
            context.redirect("/forbidden");
        var provider = ((DataResponse<Provider>)response).getData();
        var html = new File("src/main/static/Provider.html");
        var document = Jsoup.parse(html, "UTF-8");
        HtmlHelper.MakeProviderElement(document, provider);
        var row = HtmlHelper.GetCommoditiesTable(provider.getCommodities());
        var table = document.getElementsByTag("table");
        table.get(0).append(row);
        context.contentType("text/html");
        context.result(document.toString());
    }
}
