package com.baloot.services;

import com.baloot.entities.Comment;
import com.baloot.entities.User;
import com.baloot.models.UserCommodityModel;
import com.baloot.models.UsernameModel;
import com.baloot.repositories.CommentRepository;
import com.baloot.repositories.CommodityRepository;
import com.baloot.repositories.UserRepository;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {
    private final UserRepository userRepository;
    private final CommodityRepository commodityRepository;
    private final CommentRepository commentRepository;

    public UserService(UserRepository userRepository, CommodityRepository commodityRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.commodityRepository = commodityRepository;
        this.commentRepository = commentRepository;
    }

    public Response addUser(User user) {
        if (!isUserValid(user)){
            return new DataResponse<>(false, "User fields are not valid!");
        }
        userRepository.addUser(user);
        return new DataResponse<>(true, "User added.");
    }

    public Response addToBuyList(UserCommodityModel userCommodityModel) {
        var userToAdd = userRepository.findUser(userCommodityModel.getUsername());
        var commodity = commodityRepository.findCommodity(userCommodityModel.getCommodityId());
        if (commodity == null)
            return new DataResponse<>(false, "Commodity not found!");
        if (userToAdd == null)
            return new DataResponse<>(false, "User not found!");
        if (commodity.getInStock() == 0)
            return new DataResponse<>(false, "Not enough stock!");
        if (userToAdd.addToBuyList(commodity)) {
            commodity.setInStock(commodity.getInStock() - 1);
            return new DataResponse<>(true, "Commodity added to the buy list.");
        }
        return new DataResponse<>(false, "Commodity already exists in the buy list!");
    }

    public Response removeFromBuyList(UserCommodityModel userCommodityModel) {
        var userToAdd = userRepository.findUser(userCommodityModel.getUsername());
        var commodity = commodityRepository.findCommodity(userCommodityModel.getCommodityId());
        if (commodity == null)
            return new DataResponse<>(false, "Commodity not found!");
        if (userToAdd == null)
            return new DataResponse<>(false, "User not found!");
        if (userToAdd.removeFromBuyList(commodity)) {
            commodity.setInStock(commodity.getInStock() + 1);
            return new DataResponse<>(true, "Commodity removed from the buy list!");
        }
        return new DataResponse<>(false, "Commodity does not exist in the buy list.");
    }

    public Response getBuyList(UsernameModel usernameModel) {
        User userToGetBuyList = userRepository.findUser(usernameModel.getUsername());
        if (userToGetBuyList == null)
            return new DataResponse<>(false, "User not found!");
        return new DataResponse<>(true, userToGetBuyList.getBuyList());
    }

    public Response getUser(String username) {
        User user = userRepository.findUser(username);
        if (user == null)
            return new DataResponse<>(false, "User not found!");
        return new DataResponse<>(true, user);
    }

    public Response addCredit(String username, int credit) {
        User user = userRepository.findUser(username);
        if (user == null)
            return new DataResponse<>(false, "User not found!");
        user.setCredit(user.getCredit() + credit);
        return new DataResponse<>(true, "User credit updated.");
    }

    public Response voteComment(String username, int commentId, int vote){
        User user = userRepository.findUser(username);
        if (user == null)
            return new DataResponse<>(false, "User not found!");
        Comment comment = commentRepository.getComment(commentId);
        if (comment == null)
            return new DataResponse<>(false, "Comment not found!");
        comment.voteComment(vote);
        return new DataResponse<>(true, "Vote added.");
    }

    private boolean isUserValid(User user) {
        Pattern userNamePattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher userNameMatcher = userNamePattern.matcher(user.getUsername());
        return userNameMatcher.matches();
    }
}
