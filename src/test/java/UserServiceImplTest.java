import org.junit.Assert;
import org.junit.Test;
import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.services.impl.UserServiceImpl;

/**
 * Tests for UserServiceImpl
 */
public class UserServiceImplTest {

    @Test
    public void processRegistrationShouldBeReturnValidUser(){
        UserServiceImpl service = new UserServiceImpl();

        String testJsonUser = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"adam\",\"password\":\"123\"}}";
        User testUser = User.builder()
                .name("Adam")
                .lastName("Adam")
                .age(String.valueOf(29))
                .credentials(new Credentials("adam","123"))
                .build();

        Assert.assertEquals(testUser.getCredentials().toString(),
                service.processRegistration(testJsonUser).getCredentials().toString());
        Assert.assertEquals(testUser.getName(),service.processRegistration(testJsonUser).getName());
        Assert.assertEquals(testUser.getLastName(),service.processRegistration(testJsonUser).getLastName());
        Assert.assertEquals(testUser.getAge(),service.processRegistration(testJsonUser).getAge());
    }

    @Test
    public void processLoginShouldBeTrue(){
        UserServiceImpl service = new UserServiceImpl();
        String testJsonUser = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"adam\",\"password\":\"123\"}}";
        service.processRegistration(testJsonUser);
        String testJsonCredentials = "{\"login\":\"adam\",\"password\":\"123\"}";
        String adminCredentials = "{\"login\":\"adam\",\"password\":\"123\"}";

        Assert.assertTrue(service.processLogin(testJsonCredentials));
        Assert.assertTrue(service.processLogin(adminCredentials));
    }
    @Test
    public void processLoginShouldBeFalse(){
        UserServiceImpl service = new UserServiceImpl();
        String testJsonUser = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"adam\",\"password\":\"123\"}}";
        service.processRegistration(testJsonUser);
        String testJsonCredentials = "{\"login\":\"adam\",\"password\":\"1234\"}";

        Assert.assertFalse(service.processLogin(testJsonCredentials));
    }

    @Test
    public void processLogoutShouldBeTrue(){
        UserServiceImpl service = new UserServiceImpl();
        String testJsonUser = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"adam\",\"password\":\"123\"}}";
        service.processRegistration(testJsonUser);
        String testJsonCredentials = "{\"login\":\"adam\",\"password\":\"123\"}";
        service.processLogin(testJsonCredentials);

        Assert.assertTrue(service.processLogout());
    }
}
