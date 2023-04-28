package com.baloot.service;

import com.baloot.core.entities.Comment;
import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityRate;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.dataAccess.utils.QueryModel;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        var tempProvider = providerRepository.findProvider(commodity.getProviderId());
        if (tempProvider == null)
            return DataResponse.Failed("Provider not found!");
        if (commodityRepository.addCommodity(commodity))
            return DataResponse.Successful();
        return DataResponse.Failed("Commodity already exists.");
    }

    public Response getCommodityById(int commodityId) {
        Commodity commodityToFind = commodityRepository.findCommodity(commodityId);
        if (commodityToFind == null)
            return DataResponse.Failed("Commodity not found!");
        return DataResponse.Successful(commodityToFind);
    }

    public Response getCommodities(QueryModel query) {
        return DataResponse.Successful(this.commodityRepository.getCommodities(query));
    }

    public Response getComments(int commodityId) {
        Commodity commodity = commodityRepository.findCommodity(commodityId);
        if (commodity == null)
            return DataResponse.Failed("Commodity not found!");
        return DataResponse.Successful(commodity.getComments());
    }

    public Response rateCommodity(CommodityRate commodityRate) {
        Commodity commodityToRate = commodityRepository.findCommodity(commodityRate.getCommodityId());
        if (commodityToRate == null)
            return DataResponse.Failed("Commodity not found!");
        if (userRepository.findUser(commodityRate.getUsername()) == null)
            return DataResponse.Failed("User not found!");
        commodityToRate.addRating(commodityRate);
        return DataResponse.Successful();
    }

    public Response addComment(String text, String username, int commodityId) {
        var commodity = commodityRepository.findCommodity(commodityId);
        var user = userRepository.findUser(username);
        var comment = new Comment(username, user.getEmail(), commodityId, text, LocalDate.now().toString());
        commentRepository.addComment(comment);
        commodity.addComment(comment);
        return DataResponse.Successful();
    }

    public Response getSuggestions(int commodityId) {
        var commodity = commodityRepository.findCommodity(commodityId);
        if (commodity == null) {
            return DataResponse.Failed("Commodity not found!");
        }
        var commodities = commodityRepository.getCommodities(null).commodities();
        var suggestions = new ArrayList<Commodity>();
        commodities.sort((a, b) -> (int) Math.signum(calculateScore(commodity, b) - calculateScore(commodity, a)));
        for (Commodity c : commodities) {
            if (suggestions.size() == 5) {
                break;
            }
            if (c.getId() == commodityId) {
                continue;
            }
            suggestions.add(c);
        }
        return DataResponse.Successful(suggestions);
    }

    private double calculateScore(Commodity a, Commodity b) {
        boolean hasCommon = false;
        for (String category : a.getCategories()) {
            if (b.getCategories().contains(category)) {
                hasCommon = true;
                break;
            }
        }
        return b.getRating() + (hasCommon ? 1:0) * 11;
    }
}
