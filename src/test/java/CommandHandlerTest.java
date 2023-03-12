import com.baloot.CommandHandler;
import com.baloot.entities.Comment;
import com.baloot.entities.Commodity;
import com.baloot.entities.Provider;
import com.baloot.entities.User;
import com.baloot.repositories.*;
import com.baloot.services.CommodityService;
import com.baloot.services.ProviderService;
import com.baloot.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CommandHandlerTest {
    private CommandHandler commandHandler;
    public static List<User> users;
    public static List<Provider> providers;
    public static List<Commodity> commodities;
    public static Gson gson;
    @Before
    public void setupApplication() {
        var database = new Database();

        var userRepository = new UserRepository(database);
        var providerRepository = new ProviderRepository(database);
        var commodityRepository = new CommodityRepository(database);
        var commentRepository = new CommentRepository(database);

        var userService = new UserService(userRepository, commodityRepository, commentRepository);
        var providerService = new ProviderService(providerRepository);
        var commodityService = new CommodityService(providerRepository, commodityRepository, userRepository);

        commandHandler =  new CommandHandler(userService, providerService, commodityService);
    }

    @BeforeClass
    public static void setupData() throws FileNotFoundException {
        gson = new Gson();
        var reader = new JsonReader(new FileReader("src/test/resources/users.json"));
        var userListType = new TypeToken<List<User>>() {}.getType();
        users = gson.fromJson(reader, userListType);
        reader = new JsonReader(new FileReader("src/test/resources/providers.json"));
        var providerListType = new TypeToken<List<Provider>>() {}.getType();
        providers = gson.fromJson(reader, providerListType);
        reader = new JsonReader(new FileReader("src/test/resources/commodities.json"));
        var commodityListType = new TypeToken<List<Commodity>>() {}.getType();
        commodities = gson.fromJson(reader, commodityListType);
    }

    @Test
    public void addUser_NewUser_Success() {
        var response = commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":\"User added.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addUser_WrongUsername_Fail() {
        var response = commandHandler.executeCommand("addUser " + gson.toJson(users.get(1)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"User fields are not valid!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addProvider_NewProvider_Success() {
        var response = commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":\"Provider added.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addProvider_DuplicateProvider_Fail() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        var response = commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Provider id is taken!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addCommodity_NewCommodity_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        var response = commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":\"Commodity added.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addCommodity_DuplicateCommodity_Fail() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        var response = commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Commodity already exists.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addCommodity_ProviderNotExist_Fail() {
        var response = commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Provider not found!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void getCommoditiesList_CommodityAdded_ReturnCommoditiesList() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        var response = commandHandler.executeCommand("getCommoditiesList");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":[{\"id\":1,\"name\":\"name\",\"price\":1000" +
                ",\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}]}"),
                JsonParser.parseString(response));
    }

    @Test
    public void rateCommodity_NewCommodityRate_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        var response = commandHandler.executeCommand("rateCommodity {\"username\":\"username\",\"commodityId\":1" +
                ",\"score\":8}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":\"Rate added.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void rateCommodity_WrongCommodity_Fail() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        var response = commandHandler.executeCommand("rateCommodity {\"username\":\"username\",\"commodityId\":1" +
                ",\"score\":8}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Commodity not found!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void rateCommodity_WrongUser_Fail() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        var response = commandHandler.executeCommand("rateCommodity {\"username\":\"username\",\"commodityId\":1" +
                ",\"score\":8}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"User not found!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addToBuyList_NewBuyListItem_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        var response = commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":\"Commodity added to the buy list.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addToBuyList_DuplicateBuyListItem_Fail() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1}");
        var response = commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Commodity already exists in " +
                        "the buy list!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addToBuyList_BuyListItemExists_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1}");
        var response = commandHandler.executeCommand("removeFromBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":\"Commodity removed from the buy " +
                        "list!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void addToBuyList_BuyListItemNotExists_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        var response = commandHandler.executeCommand("removeFromBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Commodity does not exist in the " +
                        "buy list.\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void getCommodityById_CommodityExists_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        var response = commandHandler.executeCommand("getCommodityById {\"id\":1}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":{\"id\":1,\"name\":\"name\",\"price\":1000" +
                ",\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}}"),
                JsonParser.parseString(response));
    }

    @Test
    public void getCommodityById_CommodityNotExists_Fail() {
        var response = commandHandler.executeCommand("getCommodityById {\"id\":1}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Commodity not found!\"}"),
                JsonParser.parseString(response));
    }

    @Test
    public void getCommoditiesByCategory_NotEmptyCategory_CommoditiesList() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(1)));
        var response = commandHandler.executeCommand("getCommoditiesByCategory {\"category\":\"cat1\"}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":[{\"id\":1,\"name\":\"name\",\"price\":1000" +
                ",\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}]}"),
                JsonParser.parseString(response));
    }
    @Test
    public void getCommoditiesByCategory_EmptyCategory_EmptyCommoditiesList() {
        var response = commandHandler.executeCommand("getCommoditiesByCategory {\"category\":\"cat1\"}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":[]}"), JsonParser.parseString(response));
    }

    @Test
    public void getBuyList_UserExists_Success() {
        commandHandler.executeCommand("addProvider " + gson.toJson(providers.get(0)));
        commandHandler.executeCommand("addCommodity " + gson.toJson(commodities.get(0)));
        commandHandler.executeCommand("addUser " + gson.toJson(users.get(0)));
        commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        var response = commandHandler.executeCommand("getBuyList {\"username\":\"username\"}");
        Assert.assertEquals(JsonParser.parseString("{\"success\":true,\"data\":[{\"id\":1,\"name\":\"name\"," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":49,\"providerId\":1}]}"),
                JsonParser.parseString(response));
    }

    @Test
    public void executeCommand_InvalidCommand_Fail() {
        var response = commandHandler.executeCommand("invalidCommand");
        Assert.assertEquals(JsonParser.parseString("{\"success\":false,\"data\":\"Not a valid command!\"}"),
                JsonParser.parseString(response));
    }
}
