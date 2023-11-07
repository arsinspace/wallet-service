package IntegrationTestcontainersTest;

import config.ContainerEnvironment;
import org.junit.Assert;
import org.junit.Test;
import ru.ylab.dao.UserDAO;
import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;

public class UserRepositoryTest extends ContainerEnvironment {

    UserRepository repository = new UserDAO();

    @Test
    public void when_findUserByCredentials_expect_true() {
        Assert.assertTrue(repository.findUserByCredentials(new Credentials("adam","123")).isPresent());
    }

//    @Test
//    public void when_saveUser_expect_true() {
//        User user = User.builder()
//                .name("max")
//                .lastName("max")
//                .age("29")
//                .credentials(new Credentials("max","123"))
//                .build();
//        Assert.assertEquals(3, repository.saveUser(user));
//    }
    @Test
    public void when_findAllByUsers_expect_non_empty_list() {
        Assert.assertFalse(repository.findAllUsers().isEmpty());
    }
}
