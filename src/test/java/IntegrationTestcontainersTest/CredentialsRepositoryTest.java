package IntegrationTestcontainersTest;

import config.ContainerEnvironment;
import org.junit.Assert;
import org.junit.Test;
import ru.ylab.repository.CredentialsRepository;

public class CredentialsRepositoryTest extends ContainerEnvironment {

    @Test
    public void when_isLoginUsed_expect_true(){
        Assert.assertTrue(CredentialsRepository.isLoginUsed("adam"));
    }

    @Test
    public void when_isLoginUsed_expect_false(){
        Assert.assertFalse((CredentialsRepository.isLoginUsed("adam1")));
    }
}
