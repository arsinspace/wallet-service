import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.model.enums.TransactionStatus;
import ru.ylab.walletservice.utils.mappers.TransactionDTOMapper;

import java.sql.Timestamp;

/**
 * Mapping DTO tests
 */
public class MapperDTOTest {

    @Test
    public void convertToTransactionShouldBeReTurnValidValue(){
        Transaction transaction = Transaction.builder()
                .id(1L)
                .status(TransactionStatus.SUCCESSFUL)
                .transactionalId("123123")
                .purpose("123123123")
                .amount(123)
                .transactionalTime(new Timestamp(System.currentTimeMillis()))
                .build();
        TransactionDTO testDto = new TransactionDTO("validId","toId",123);
        TransactionDTO dto = TransactionDTOMapper.INSTANCE.toDto(transaction);
        Transaction toTransaction = TransactionDTOMapper.INSTANCE.toTransaction(testDto);
        Assertions.assertThat(dto.getTransactionalId())
                .isEqualTo(transaction.getTransactionalId());
        Assertions.assertThat(toTransaction.getAmount()).isEqualTo(123);
    }
}
