package com.baloot;

import com.baloot.entities.Comment;
import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HtmlHelper {
    public static String GetCommentsTable(List<Comment> comments, String username) {
        StringBuilder result = new StringBuilder();
        for (var comment : comments) {
            result.append(GetCommentRow(comment, username));
        }
        return result.toString();
    }

    public static String GetCommentRow(Comment comment, String username) {
        var res =
                "<tr>" +
                        "<td>" + comment.getUsername() + "</td>" +
                        "<td>" + comment.getText() + "</td>" +
                        "<td>" + comment.getDate() + "</td>";
        if (username != null) {
            res +=
                    "<td>" +
                            "<form action=\"/voteComment/" + comment.getId() + "\" method=\"POST\">" +
                            "<label for=\"\">" + comment.getLikes() + "</label>" +
                            "<input id=\"like_vote\" type=\"hidden\" name=\"vote\" value=\"1\"/>" +
                            "<input id=\"like_username\" type=\"hidden\" name=\"username\" value=\"" + username + "\"/>" +
                            "<button type=\"submit\">like</button>" +
                            "</form>" +
                            "</td>";
            res +=
                    "<td>" +
                            "<form action=\"/voteComment/" + comment.getId() + "\" method=\"POST\">" +
                            "<label for=\"\">" + comment.getDislikes() + "</label>" +
                            "<input id=\"dislike_vote\" type=\"hidden\" name=\"vote\" value=\"-1\"/>" +
                            "<input id=\"dislike_username\" type=\"hidden\" name=\"username\" value=\"" + username + "\"/>" +
                            "<button type=\"submit\">dislike</button>" +
                            "</form>" +
                            "</td>";
        } else {
            res +=
                    "<td>" +
                            "<button disabled>like</button>" +
                            "</td>";
            res +=
                    "<td>" +
                            "<button disabled>dislike</button>" +
                            "</td>";
        }
        res += "</tr>";
        return res;
    }

    public static String GetBuyList(User user) {
        StringBuilder result = new StringBuilder();
        for (var commodity : user.getBuyList()) {
            result.append(GetBuyListRow(commodity, user.getUsername()));
        }
        return result.toString();
    }

    private static String GetBuyListRow(Commodity commodity, String username) {
        return "<tr>" + "<th>" + commodity.getId() + "</td>" +
                "<td>" + commodity.getName() + "</td>" +
                "<td>" + commodity.getProviderId() + "</td>" +
                "<td>" + commodity.getPrice() + "</td>" +
                "<td>" + commodity.getCategoryString() + "</td>" +
                "<td>" + commodity.getRating() + "</td>" +
                "<td>" + commodity.getInStock() + "</td>" +
                "<td><a href=\"/commodities/" + commodity.getId() + "\">Link</a></td>" +
                "<td>" +
                "<form action=\"/removeFromBuyList/" + username + "/" + commodity.getId() + "\" method=\"GET\" >" +
                "<button type=\"submit\">Remove</button>" +
                "</form>" +
                "</td>" + "</tr>";
    }

    public static String GetCommoditiesTable(List<Commodity> commodities) {
        String result = "";
        for (var commodity : commodities) {
            result += GetCommodityRow(commodity);
        }
        return result;
    }

    private static String GetCommodityRow(Commodity commodity) {
        return "<tr>" +
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
        document.getElementsByTag("table").append(GetBuyList(user));
        document.getElementById("form_credit").attr("action", "/addCredit/" + user.getUsername() + "/");
    }
}
