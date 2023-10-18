import config.ContainerEnvironment;
import org.junit.Assert;
import org.junit.Test;
import ru.ylab.dao.TransactionDAO;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.services.impl.TransactionalServiceImpl;

/**
 * Tests for TransactionServiceImpl
 */
public class TransactionServiceImplTest extends ContainerEnvironment {

    @Test
    public void processDebitAndCreditTransactionsShouldBeTrue(){

        TransactionalServiceImpl service = new TransactionalServiceImpl(new TransactionDAO());
        String testStringJsonTransaction0 = "{\"transactionalId\":\"yourId11\",\"purpose\":\"example\",\"amount\":123}";
        String testStringJsonTransaction1 = "{\"transactionalId\":\"yourId12\",\"purpose\":\"example\",\"amount\":123}";
        User testUser = User.builder()
                .name("Adam")
                .lastName("Adam")
                .wallet(new Wallet(123))
                .credentials(null)
                .build();
        Assert.assertTrue(service.processCreditTransaction(testStringJsonTransaction0, testUser));
        Assert.assertTrue(service.processDebitTransaction(testStringJsonTransaction1,testUser));
    }

    @Test
    public void processDebitAndCreditTransactionsShouldBeFalse(){
        TransactionalServiceImpl service = new TransactionalServiceImpl(new TransactionDAO());
        String testStringJsonTransaction0 = "{\"transactionalId\":\"yourId\",\"purpose\":\"example\",\"amount\":123}";
        String testStringJsonTransaction1 = "{\"transactionalId\":\"yourId\",\"purpose\":\"example\",\"amount\":123}";
        User testUser = User.builder()
                .name("Adam")
                .lastName("Adam")
//                .id(null)
                .credentials(null)
                .build();
        Assert.assertFalse(service.processDebitTransaction(testStringJsonTransaction0,testUser));
        Assert.assertFalse(service.processCreditTransaction(testStringJsonTransaction1,testUser));
    }
}
