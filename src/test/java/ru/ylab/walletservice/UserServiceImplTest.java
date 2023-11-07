import config.ContainerEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import ru.ylab.dao.UserDAO;
import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.services.UserService;
import ru.ylab.services.impl.UserServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for UserServiceImpl
 */
public class UserServiceImplTest extends ContainerEnvironment {

    private UserService service;
    private User testUser;

    @BeforeEach
    void setUp(){
        service = new UserServiceImpl(new UserDAO());
        testUser = mock(User.class);
        when(testUser.getName()).thenReturn("Adam");
        when(testUser.getCredentials()).thenReturn(new Credentials("testLogin","123"));
    }

    @Test
    public void processRegistrationShouldBeReturnValidUser(){

        String testJsonUser = "{\"name\":\"TestName\",\"lastName\":\"TestLastName\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"testLogin\",\"password\":\"123\"}}";

        Assertions.assertThat(service.processRegistration(testJsonUser).getCredentials().toString())
                .isEqualTo(testUser.getCredentials().toString());

    }

    @Test
    public void processLoginUserShouldBeTrue(){

        String testJsonCredentials = "{\"login\":\"adam\",\"password\":\"123\"}";

        Assertions.assertThat(service.processLogin(testJsonCredentials)).isTrue();
    }

    @Test
    public void processLoginAdminShouldBeTrue(){

        String adminCredentials = "{\"login\":\"admin\",\"password\":\"admin\"}";

        Assertions.assertThat(service.processLogin(adminCredentials)).isTrue();
    }
    @Test
    public void processLoginShouldBeFalse(){

        String testJsonCredentials = "{\"login\":\"adam\",\"password\":\"12348756\"}";

        Assertions.assertThat(service.processLogin(testJsonCredentials)).isFalse();

    }

    @Test
    public void processLogoutShouldBeTrue(){
        service.processLogin("{\"login\":\"admin\",\"password\":\"admin\"}");
        service.processLogout();
        
        Assertions.assertThat(service.getCurrentAppUser()).isNull();
    }
}
