package com.baloot.service.models;

import com.baloot.core.entities.Comment;
import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.Provider;
import com.baloot.core.entities.User;
import com.baloot.presentation.models.ProviderModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {
    public static CommentModel convertToModel(Comment comment) {
        return new CommentModel(comment.getId(), comment.getText(), comment.getUser().getUsername(), comment.getLikes(),
            comment.getDislikes(), comment.getDate());
    }
    public static List<CommentModel> convertToModel(Set<Comment> comments) {
        return comments.stream().map(Converter::convertToModel).toList();
    }

    public static CommodityItemModel convertToModel(Commodity commodity, User user) {
        var item = user.getBuyList().stream()
            .filter(c -> c.getCommodity().getId() == commodity.getId())
            .findFirst()
            .orElse(null);
        int inCart = (item == null) ? 0 : item.getInCart();
        return new CommodityItemModel(convertToModel(commodity), inCart);
    }

    public static CommodityModel convertToModel(Commodity commodity) {
        return new CommodityModel(commodity.getId(), commodity.getName(), commodity.getPrice(), commodity.getImage(),
            commodity.getRating(), commodity.getRateCount(), commodity.getInStock(), commodity.getProvider().getName(),
            commodity.getProvider().getId(), commodity.getCategories());
    }

    public static ProviderModel convertToModel(Provider provider, User user) {
        var commodities = provider.getCommodities().stream().map(c -> convertToModel(c, user))
            .collect(Collectors.toList());
        return new ProviderModel(provider.getName(), provider.getImage(), provider.getRegistryDate(), commodities);
    }

    public static UserModel convertToModel(User user) {
        var buyList = user.getBuyList().stream().map(c -> new CommodityItemModel(convertToModel(c.getCommodity()),
            c.getInCart())).toList();
        var purchased = user.getPurchased().stream().map(c -> new CommodityItemModel(convertToModel(c.getCommodity()),
            c.getInCart())).toList();
        return new UserModel(user.getUsername(), user.getCredit(), user.getEmail(), user.getAddress(),
            user.getBirthDate(), buyList, purchased);
    }
}
