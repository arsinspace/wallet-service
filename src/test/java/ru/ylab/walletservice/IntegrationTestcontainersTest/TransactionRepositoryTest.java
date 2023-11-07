package IntegrationTestcontainersTest;

import config.ContainerEnvironment;
import org.junit.Assert;
import org.junit.Test;
import ru.ylab.dao.TransactionDAO;
import ru.ylab.repository.TransactionalRepository;

public class TransactionRepositoryTest extends ContainerEnvironment {
    TransactionalRepository repository = new TransactionDAO();

    @Test
    public void when_anyMatchTransactionalById_expect_true() {

        Assert.assertTrue(repository.anyMatchTransactionalById("yourId"));
    }

    @Test
    public void when_anyMatchTransactionalById_expect_false() {
        Assert.assertFalse(repository.anyMatchTransactionalById("yourId1"));
    }

    @Test
    public void when_findAllByUserId_expect_non_empty_list() {
        Assert.assertFalse(repository.findAllByUserId(1L).isEmpty());
    }

//    @Test
//    public void when_save_transaction_expect_long_value() {
//        Transactional transaction = Transactional.builder()
//                .status(TransactionStatus.SUCCESSFUL)
//                .transactionalId("123123123")
//                .amount(20)
//                .purpose("test")
//                .userId(1)
//                .build();
//        Assert.assertEquals(4, repository.saveTransaction(transaction));
//    }
}
