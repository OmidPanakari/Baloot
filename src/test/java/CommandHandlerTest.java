import com.baloot.CommandHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandHandlerTest {
    private CommandHandler commandHandler;
    @Before
    public void createCommandHandler() {
        commandHandler = new CommandHandler();
    }

    @Test
    public void addUser_NewUser_Success() {
        var response = commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":\"User added.\"}");
    }

    @Test
    public void addUser_WrongUsername_Fail() {
        var response = commandHandler.executeCommand("addUser {\"username\":\"___\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"User fields are not valid!\"}");
    }

    @Test
    public void addProvider_NewProvider_Success() {
        var response = commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\"" +
                ":\"2023-09-15\"}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":\"Provider added.\"}");
    }

    @Test
    public void addProvider_DuplicateProvider_Fail() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\"" +
                ":\"2023-09-15\"}");
        var response = commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\"" +
                ":\"2023-09-15\"}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Provider id is taken!\"}");
    }

    @Test
    public void addCommodity_NewCommodity_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        var response = commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":\"Commodity added.\"}");
    }

    @Test
    public void addCommodity_DuplicateCommodity_Fail() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        var response = commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Commodity already exists.\"}");
    }

    @Test
    public void addCommodity_ProviderNotExist_Fail() {
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        var response = commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Provider not found!\"}");
    }

    @Test
    public void getCommoditiesList_CommodityAdded_ReturnCommoditiesList() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        var response = commandHandler.executeCommand("getCommoditiesList");
        Assert.assertEquals(response, "{\"success\":true,\"data\":[{\"id\":1,\"name\":\"name\",\"price\":1000" +
                ",\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}]}");
    }

    @Test
    public void rateCommodity_NewCommodityRate_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        var response = commandHandler.executeCommand("rateCommodity {\"username\":\"username\",\"commodityId\":1" +
                ",\"score\":8}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":\"Rate added.\"}");
    }

    @Test
    public void rateCommodity_WrongCommodity_Fail() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        var response = commandHandler.executeCommand("rateCommodity {\"username\":\"username\",\"commodityId\":1" +
                ",\"score\":8}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Commodity not found!\"}");
    }

    @Test
    public void rateCommodity_WrongUser_Fail() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        var response = commandHandler.executeCommand("rateCommodity {\"username\":\"username\",\"commodityId\":1" +
                ",\"score\":8}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"User not found!\"}");
    }

    @Test
    public void addToBuyList_NewBuyListItem_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        var response = commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":\"Commodity added to the buy list.\"}");
    }

    @Test
    public void addToBuyList_DuplicateBuyListItem_Fail() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1}");
        var response = commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Commodity already exists in the buy list!\"}");
    }

    @Test
    public void addToBuyList_BuyListItemExists_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1}");
        var response = commandHandler.executeCommand("removeFromBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":\"Commodity removed from the buy list!\"}");
    }

    @Test
    public void addToBuyList_BuyListItemNotExists_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        var response = commandHandler.executeCommand("removeFromBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Commodity does not exist in the buy list.\"}");
    }

    @Test
    public void getCommodityById_CommodityExists_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        var response = commandHandler.executeCommand("getCommodityById {\"id\":1}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":{\"id\":1,\"name\":\"name\",\"price\":1000" +
                ",\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}}");
    }

    @Test
    public void getCommodityById_CommodityNotExists_Fail() {
        var response = commandHandler.executeCommand("getCommodityById {\"id\":1}");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Commodity not found!\"}");
    }

    @Test
    public void getCommoditiesByCategory_NotEmptyCategory_CommoditiesList() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        var response = commandHandler.executeCommand("getCommoditiesByCategory {\"category\":\"cat1\"}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":[{\"id\":1,\"name\":\"name\",\"price\":1000" +
                ",\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}]}");
    }
    @Test
    public void getCommoditiesByCategory_EmptyCategory_EmptyCommoditiesList() {
        var response = commandHandler.executeCommand("getCommoditiesByCategory {\"category\":\"cat1\"}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":[]}");
    }

    @Test
    public void getBuyList_UserExists_Success() {
        commandHandler.executeCommand("addProvider {\"id\":1,\"name\":\"name\",\"registryDate\":\"2023-09-15\"}");
        commandHandler.executeCommand("addCommodity {\"id\":1,\"name\":\"name\",\"providerId\":1," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":1.0,\"inStock\":50}");
        commandHandler.executeCommand("addUser {\"username\":\"username\",\"password\":\"pass\"," +
                "\"email\":\"user@email.com\",\"birthDate\":\"1977-09-17\",\"address\":\"address\",\"credit\":1500}");
        commandHandler.executeCommand("addToBuyList {\"username\":\"username\",\"commodityId\":1" +
                "}");
        var response = commandHandler.executeCommand("getBuyList {\"username\":\"username\"}");
        Assert.assertEquals(response, "{\"success\":true,\"data\":{\"buyList\":[{\"id\":1,\"name\":\"name\"," +
                "\"price\":1000,\"categories\":[\"cat1\",\"cat2\"],\"rating\":0.0,\"inStock\":50,\"providerId\":1}]}}");
    }

    @Test
    public void executeCommand_InvalidCommand_Fail() {
        var response = commandHandler.executeCommand("invalidCommand");
        Assert.assertEquals(response, "{\"success\":false,\"data\":\"Not a valid command!\"}");
    }
}
