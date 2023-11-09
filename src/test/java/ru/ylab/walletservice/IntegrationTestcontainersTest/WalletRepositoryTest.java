package IntegrationTestcontainersTest;

import config.ContainerEnvironment;
import org.junit.Test;
import ru.ylab.repository.WalletRepository;

public class WalletRepositoryTest extends ContainerEnvironment {

    @Test
    public void when_findAllUserActions_expect_true() {
        WalletRepository.updateBalance(1L,1);
    }
}
