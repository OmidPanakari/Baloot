import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityRating;
import com.baloot.core.entities.Provider;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.ProviderRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.presentation.models.RateModel;
import com.baloot.responses.DataResponse;
import com.baloot.service.CommodityService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommodityServiceTest {
    @Mock
    ProviderRepository providerRepositoryMock;
    @Mock
    CommodityRepository commodityRepositoryMock;
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    CommentRepository commentRepository;
    private CommodityService commodityService;
    public static List<User> users;
    public static List<Provider> providers;
    public static List<Commodity> commodities;
    public static Gson gson;

    @BeforeEach
    public void setup() throws Exception{
        commodityService = new CommodityService(providerRepositoryMock, commodityRepositoryMock, userRepositoryMock, commentRepository);
    }

    @BeforeAll
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
    public void rateCommodity_CommodityNotExists_Fail() {
        // Arrange
        var commodity = commodities.get(0);
        var user = users.get(0);
        when(commodityRepositoryMock.findCommodity(commodity.getId()))
                .thenReturn(null);
        // Action
        var response = commodityService.rateCommodity(new CommodityRating(user.getUsername(), commodity.getId(), 8));
        // Assert
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Commodity not found!", response.getMessage());
    }

    @Test
    public void rateCommodity_UserNotExists_Fail() {
        // Arrange
        var commodity = commodities.get(0);
        var user = users.get(0);
        when(commodityRepositoryMock.findCommodity(commodity.getId()))
                .thenReturn(commodity);
        when(userRepositoryMock.findUser(user.getUsername()))
                .thenReturn(null);
        // Action
        var response = commodityService.rateCommodity(new CommodityRating(user.getUsername(), commodity.getId(), 8));
        // Assert
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("User not found!", response.getMessage());
    }
}
