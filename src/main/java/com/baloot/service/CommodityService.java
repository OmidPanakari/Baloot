package com.baloot.service;

import com.baloot.core.entities.Comment;
import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityRating;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.models.CommodityList;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.dataAccess.utils.QueryModel;
import com.baloot.presentation.models.RateModel;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.baloot.service.models.CommodityListModel;
import com.baloot.service.models.CommodityItemModel;
import com.baloot.service.models.CommodityModel;
import com.baloot.service.models.Converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommodityService {
    private final ProviderRepository providerRepository;
    private final CommodityRepository commodityRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public CommodityService(ProviderRepository providerRepository, CommodityRepository commodityRepository,
                            UserRepository userRepository, CommentRepository commentRepository) {
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Response addCommodity(Commodity commodity) {
        var tempProvider = providerRepository.findProvider(commodity.getProvider().getId());
        if (tempProvider == null)
            return DataResponse.Failed("Provider not found!");
        if (commodityRepository.addCommodity(commodity))
            return DataResponse.Successful();
        return DataResponse.Failed("Commodity already exists.");
    }

    public Response getCommodityById(int commodityId, String username) {
        var user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        Commodity commodityToFind = commodityRepository.findCommodity(commodityId);
        if (commodityToFind == null)
            return DataResponse.Failed("Commodity not found!");
        return DataResponse.Successful(convertToModel(commodityToFind, user));
    }

    public Response getCommodities(QueryModel query, String username) {
        var user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        return DataResponse.Successful(convertToModel(commodityRepository.getCommodities(query), user));
    }

    public Response getComments(int commodityId) {
        Commodity commodity = commodityRepository.findCommodity(commodityId);
        if (commodity == null)
            return DataResponse.Failed("Commodity not found!");
        return DataResponse.Successful(Converter.convertToModel(commodity.getComments()));
    }

    public Response rateCommodity(CommodityRating commodityRate) {
        Commodity commodityToRate = commodityRepository.findCommodity(commodityRate.getCommodity().getId());
        if (commodityToRate == null)
            return DataResponse.Failed("Commodity not found!");
        var user = userRepository.findUser(commodityRate.getUser().getUsername());
        if (user == null)
            return DataResponse.Failed("User not found!");
        commodityRate = commodityRepository.rateCommodity(commodityToRate, user, commodityRate.getRating());
        return DataResponse.Successful(new RateModel(commodityRate.getCommodity().getRating(),
            commodityRate.getCommodity().getRateCount()));
    }

    public Response addComment(String text, String username, int commodityId) {
        var commodity = commodityRepository.findCommodity(commodityId);
        if (commodity == null) {
            return DataResponse.Failed("Commodity not found!");
        }
        var user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        var comment = new Comment(username, commodityId, text, LocalDate.now().toString());
        commentRepository.addComment(comment);
        return DataResponse.Successful();
    }

    public Response getSuggestions(int commodityId, String username) {
        var user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        var commodity = commodityRepository.findCommodity(commodityId);
        if (commodity == null) {
            return DataResponse.Failed("Commodity not found!");
        }
        var suggestions = commodityRepository.getSuggestions(commodity);
        return DataResponse.Successful(convertToModel(suggestions, user));
    }

    private CommodityItemModel convertToModel(Commodity commodity, User user) {
        var item = user.getBuyList().stream()
                .filter(c -> c.getCommodity().getId() == commodity.getId())
                .findFirst()
                .orElse(null);
        int inCart = (item == null) ? 0 : item.getInCart();
        return new CommodityItemModel(convertToModel(commodity), inCart);
    }

    private CommodityModel convertToModel(Commodity commodity) {
        return new CommodityModel(commodity.getId(), commodity.getName(), commodity.getPrice(), commodity.getImage(),
            commodity.getRating(), commodity.getRateCount(), commodity.getInStock(), commodity.getProvider().getName(),
            commodity.getProvider().getId(), commodity.getCategories());
    }

    private List<CommodityItemModel> convertToModel(List<Commodity> list, User user) {
        return list.stream().map(c -> convertToModel(c, user)).collect(Collectors.toList());
    }

    private CommodityListModel convertToModel(CommodityList list, User user) {
        var models = list.commodities().stream().map(c -> convertToModel(c, user)).collect(Collectors.toList());
        return new CommodityListModel(models, list.pageCount());
    }
}
