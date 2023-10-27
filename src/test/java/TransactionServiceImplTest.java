import config.ContainerEnvironment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.dao.TransactionDAO;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.services.impl.TransactionalServiceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for TransactionServiceImpl
 */
public class TransactionServiceImplTest extends ContainerEnvironment {

    private TransactionalServiceImpl service;
    private User testUser;
    @BeforeEach
    void setUp(){
        service = new TransactionalServiceImpl(new TransactionDAO());
        testUser = mock(User.class);
        when(testUser.getWallet()).thenReturn(new Wallet(123));
    }

    @Test
    public void processDebitAndCreditTransactionsShouldBeTrue(){

        String testStringJsonTransaction0 = "{\"transactionalId\":\"yourId16\",\"purpose\":\"example\",\"amount\":123}";
        String testStringJsonTransaction1 = "{\"transactionalId\":\"yourId17\",\"purpose\":\"example\",\"amount\":123}";

        Assertions.assertThat(service.processCreditTransaction(testStringJsonTransaction0,testUser)).isTrue();
        Assertions.assertThat(service.processCreditTransaction(testStringJsonTransaction1,testUser)).isTrue();
    }

    @Test
    public void processDebitAndCreditTransactionsShouldBeFalse(){

        String testStringJsonTransaction0 = "{\"transactionalId\":\"yourId\",\"purpose\":\"example\",\"amount\":123}";
        String testStringJsonTransaction1 = "{\"transactionalId\":\"yourId\",\"purpose\":\"example\",\"amount\":123}";

        Assertions.assertThat(service.processDebitTransaction(testStringJsonTransaction0,testUser)).isFalse();
        Assertions.assertThat(service.processCreditTransaction(testStringJsonTransaction1,testUser)).isFalse();
    }
}
