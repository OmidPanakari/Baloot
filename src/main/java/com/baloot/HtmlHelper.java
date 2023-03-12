package com.baloot;

import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import org.jsoup.nodes.Document;

import java.util.List;

public class HtmlHelper {
    public static String GetCommoditiesTable(List<Commodity> commodities) {
        String result = "";
        for (var commodity : commodities) {
            result += GetCommodityRow(commodity);
        }
        return result;
    }

    private static String GetCommodityRow(Commodity commodity) {
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

    public static void MakeCommodityElement(Document document, Commodity commodity) {
        document.getElementById("id").text("Id: " + commodity.getId());
        document.getElementById("name").text("Name " + commodity.getName());
        document.getElementById("providerId").text("Provider Id: " + commodity.getProviderId());
        document.getElementById("price").text("Price: " + commodity.getPrice());
        document.getElementById("categories").text("Categories: " + String.join(",", commodity.getCategories()));
        document.getElementById("rating").text("Rating: " + commodity.getRating());
        document.getElementById("inStock").text("In Stock: " + commodity.getInStock());
    }

    public static void MakeProviderElement(Document document, Provider provider) {
        document.getElementById("id").text("Id: " + provider.getId());
        document.getElementById("name").text("Name: " + provider.getName());
        document.getElementById("registryDate").text("Registry Date: " + provider.getRegistryDate());
    }

    public static void MakeUserElement(Document document, User user) {
        document.getElementById("username").text("Username: " + user.getUsername());
        document.getElementById("email").text("Email: " + user.getEmail());
        document.getElementById("birthDate").text("Birth Date: " + user.getBirthDate());
        document.getElementById("address").text(user.getAddress());
        document.getElementById("credit").text("Credit: " + user.getCredit());
    }
}
