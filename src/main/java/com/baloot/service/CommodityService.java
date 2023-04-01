package com.baloot.service;

import com.baloot.core.entities.Comment;
import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityRate;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;

import java.time.LocalDate;

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
            return new DataResponse<>(false, "Provider not found!");
        if (commodityRepository.addCommodity(commodity))
            return new DataResponse<>(true, "Commodity added.");
        return new DataResponse<>(false, "Commodity already exists.");
    }

    public Response getCommodityById(int commodityId) {
        Commodity commodityToFind = commodityRepository.findCommodity(commodityId);
        if (commodityToFind == null)
            return new DataResponse<>(false, "Commodity not found!");
        return new DataResponse<>(true, commodityToFind);
    }

    public Response getCommodities() {
        return new DataResponse<>(true, this.commodityRepository.getCommodities());
    }

    public Response getCommoditiesByCategory(String category) {
        var response = commodityRepository.getCommoditiesByCategory(category);
        return new DataResponse<>(true, response);
    }

    public Response getCommoditiesByPrice(int startPrice, int endPrice) {
        var response = commodityRepository.getCommoditiesByPrice(startPrice, endPrice);
        return new DataResponse<>(true, response);
    }

    public Response rateCommodity(CommodityRate commodityRate) {
        Commodity commodityToRate = commodityRepository.findCommodity(commodityRate.getCommodityId());
        if (commodityToRate == null)
            return new DataResponse<>(false, "Commodity not found!");
        if (userRepository.findUser(commodityRate.getUsername()) == null)
            return new DataResponse<>(false, "User not found!");
        commodityToRate.addRating(commodityRate);
        return new DataResponse<>(true, "Rate added.");
    }

    public Response addComment(String text, String username, int commodityId) {
        var commodity = commodityRepository.findCommodity(commodityId);
        var user = userRepository.findUser(username);
        var comment = new Comment(username, user.getEmail(), commodityId, text, LocalDate.now().toString());
        commentRepository.addComment(comment);
        commodity.addComment(comment);
        return new DataResponse<>(true, "Comment added.");
    }
}
