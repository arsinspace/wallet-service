import jakarta.validation.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.utils.mappers.JsonConverter;
import ru.ylab.walletservice.utils.validatos.TransactionDTOValidator;
import ru.ylab.walletservice.utils.validatos.UserValidator;

import static org.mockito.Mockito.when;

/**
 * Entities validation tests
 */
public class ValidatorTest {

    @Test
    public void validatorIValidTest(){

        Assertions.assertThatThrownBy(() -> {
            TransactionDTO testDto = Mockito.mock(TransactionDTO.class);
            when(testDto.getTransactionalId()).thenReturn("12345678901");
            TransactionDTOValidator.getInstance().isValid(testDto);

            User testUser = Mockito.mock(User.class);
            when(testUser.getId()).thenReturn(1L);
            UserValidator.getInstance().isValid(testUser);
        }).isInstanceOf(ValidationException.class);

        TransactionDTO testDto = new TransactionDTO("1234567890","toId",123);
        Assertions.assertThat(TransactionDTOValidator.getInstance().isValid(testDto)).isTrue();

        String userJson = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\"," +
                "\"credentials\":{\"login\":\"adam\",\"password\":\"123\"}}";
        User testUser = JsonConverter.convertToObject(User.class,userJson);

        Assertions.assertThat(UserValidator.getInstance().isValid(testUser)).isTrue();

    }


}
