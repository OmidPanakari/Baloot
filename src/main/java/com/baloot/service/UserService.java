package com.baloot.service;

import com.baloot.core.entities.Comment;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.repositories.DiscountRepository;
import com.baloot.dataAccess.utils.HashHelper;
import com.baloot.presentation.models.CartModel;
import com.baloot.presentation.models.GithubDataModel;
import com.baloot.presentation.models.GithubUserModel;
import com.baloot.presentation.utils.TokenManager;
import com.baloot.service.models.CommodityItemModel;
import com.baloot.service.models.Converter;
import com.baloot.service.models.UserCommodityModel;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.responses.DataResponse;
import com.baloot.responses.Response;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    private final String ClientSecret = "6f9aa96fc2898a8ea3ce4325952d47898eff8454";
    private final String ClientID = "Iv1.807e4d8450f5fa8f";
    private final UserRepository userRepository;
    private final CommodityRepository commodityRepository;
    private final CommentRepository commentRepository;
    private final DiscountRepository discountRepository;

    public UserService(UserRepository userRepository, CommodityRepository commodityRepository, CommentRepository commentRepository, DiscountRepository discountRepository) {
        this.userRepository = userRepository;
        this.commodityRepository = commodityRepository;
        this.commentRepository = commentRepository;
        this.discountRepository = discountRepository;
    }

    public Response addUser(User user) {
        if (!isUserValid(user)){
            return DataResponse.Failed("User fields are not valid!");
        }
        if (userRepository.findUserByEmail(user.getEmail()) != null){
            return DataResponse.Failed("This email is already exists!");
        }
        userRepository.addUser(user);
        return DataResponse.Successful();
    }

    public Response addToBuyList(UserCommodityModel userCommodityModel) {
        var userToAdd = userRepository.findUser(userCommodityModel.getUsername());
        var commodity = commodityRepository.findCommodity(userCommodityModel.getCommodityId());
        if (commodity == null)
            return DataResponse.Failed("Commodity not found!");
        if (userToAdd == null)
            return DataResponse.Failed("User not found!");
        if (commodity.getInStock() == 0)
            return DataResponse.Failed("Not enough stock!");
        userRepository.addToBuyList(userToAdd, commodity);
        return DataResponse.Successful(commodity.getInStock());
    }

    public Response removeFromBuyList(UserCommodityModel userCommodityModel) {
        var userToAdd = userRepository.findUser(userCommodityModel.getUsername());
        var commodity = commodityRepository.findCommodity(userCommodityModel.getCommodityId());
        if (commodity == null)
            return DataResponse.Failed("Commodity not found!");
        if (userToAdd == null)
            return DataResponse.Failed("User not found!");
        if (userRepository.removeFromBuyList(userToAdd, commodity)) {
            return DataResponse.Successful(commodity.getInStock());
        }
        return DataResponse.Failed("Commodity does not exist in the buy list.");
    }

    public Response getCart(String username) {
        User user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        var buyList = user.getBuyList().stream().map(c -> new CommodityItemModel(Converter.convertToModel(c.getCommodity()),
            c.getInCart())).toList();
        var purchasedList = user.getPurchased().stream().map(c -> new CommodityItemModel(Converter.convertToModel(c.getCommodity()),
            c.getInCart())).toList();
        return DataResponse.Successful(new CartModel(buyList, purchasedList));
    }

    public Response getUser(String username) {
        User user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        return DataResponse.Successful(Converter.convertToModel(user));
    }

    public Response addCredit(String username, int credit) {
        var result = userRepository.addCredit(username, credit);
        if (!result)
            return DataResponse.Failed("User not found!");
        return DataResponse.Successful("User credit updated.");
    }

    public Response voteComment(String username, int commentId, int vote){
        User user = userRepository.findUser(username);
        if (user == null)
            return DataResponse.Failed("User not found!");
        Comment comment = commentRepository.getComment(commentId);
        if (comment == null)
            return DataResponse.Failed("Comment not found!");
        comment = commentRepository.voteComment(comment, user, vote);
        return DataResponse.Successful(Converter.convertToModel(comment));
    }

    public Response purchaseBuyList(String username, String discountCode) {
        var user = userRepository.findUser(username);
        var discount = discountRepository.getDiscount(discountCode);
        if (user == null)
            return DataResponse.Failed("User not found!");
        if (!userRepository.purchaseBuyList(user, discount))
            return DataResponse.Failed("Not enough credit!");
        return DataResponse.Successful();
    }

    public Response login(String username, String password) {
        var user = userRepository.findUser(username);
        if (user == null || !user.getPassword().equals(password)) {
            return DataResponse.Failed("Username or password is not correct!");
        }
        return DataResponse.Successful();
    }

    private boolean isUserValid(User user) {
        Pattern userNamePattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher userNameMatcher = userNamePattern.matcher(user.getUsername());
        return userNameMatcher.matches();
    }

    public Response githubOAuth(String code){
        String accessToken = "";
        GithubUserModel user = null;
        try {
            URL url = new URL("https://github.com/login/oauth/access_token?client_id="+ ClientID +"&client_secret="+ClientSecret+"&code=" + code);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK)
                return DataResponse.Failed("Failed to Connect to Github!");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            Gson gson = new Gson();
            GithubDataModel responseData = gson.fromJson(String.valueOf(response), GithubDataModel.class);
            if (responseData.error != null)
                return DataResponse.Failed(responseData.error);
            accessToken = responseData.access_token;
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.Failed("Failed to Connect to Github!");
        }
        try {
            URL url = new URL("https://api.github.com/user");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "token " + accessToken);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK)
                return DataResponse.Failed("Failed to Connect to Github!");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            Gson gson = new Gson();
            GithubUserModel responseData = gson.fromJson(String.valueOf(response), GithubUserModel.class);
            user = responseData;
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.Failed("Failed to Connect to Github!");
        }

        var tempUser = userRepository.findUserByEmail(user.email);
        User newUser = new User(HashHelper.DoubleSha256("1234"), user.login, user.name, "Tehran", user.created_at, 0);
        if (tempUser == null){
            userRepository.addUser(newUser);
        } else {
            userRepository.updateUser(tempUser, newUser);
        }
        return DataResponse.Successful(TokenManager.generateToken(newUser.getUsername()));
    }
}
