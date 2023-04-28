import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.Provider;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.repositories.DiscountRepository;
import com.baloot.dataAccess.repositories.CommentRepository;
import com.baloot.dataAccess.repositories.CommodityRepository;
import com.baloot.dataAccess.repositories.UserRepository;
import com.baloot.responses.DataResponse;
import com.baloot.service.UserService;
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
public class UserServiceTest {
    @Mock
    CommentRepository commentRepositoryMock;
    @Mock
    CommodityRepository commodityRepositoryMock;
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    DiscountRepository discountRepository;
    private UserService userService;
    public static List<User> users;
    public static List<Provider> providers;
    public static List<Commodity> commodities;
    public static Gson gson;

    @BeforeEach
    public void setup() throws Exception{
        userService = new UserService(userRepositoryMock, commodityRepositoryMock, commentRepositoryMock, discountRepository);
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
    public void getBuyList_Valid_Success() {
        // Arrange
        var user = new User(users.get(0));
        user.addToBuyList(commodities.get(0));
        when(userRepositoryMock.findUser(user.getUsername()))
                .thenReturn(user);
        // Action
        var response = userService.getBuyList(user.getUsername());
        // Assert
        Assertions.assertTrue(response.isSuccess());
        var data = ((DataResponse<List<Commodity>>)response).getData();
        Assertions.assertEquals(1, data.size());
        Assertions.assertEquals(commodities.get(0).getId(), data.get(0).getId());
    }

    @Test
    public void getBuyList_UserNotExists_Fail() {
        // Arrange
        var user = new User(users.get(0));
        when(userRepositoryMock.findUser(user.getUsername()))
                .thenReturn(null);
        // Action
        var response = userService.getBuyList(user.getUsername());
        // Assert
        Assertions.assertFalse(response.isSuccess());
        var data = ((DataResponse<String>)response).getData();
        Assertions.assertEquals("User not found!", data);
    }
}
