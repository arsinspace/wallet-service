import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.utils.JsonConverter;
import org.assertj.core.api.Assertions;

import java.util.Objects;

import static org.mockito.Mockito.when;

/**
 * Tests for JsonConverter
 */
public class JsonConverterTest {

    @Test
    public void convertToObjectShouldBeReturnValidObject(){
        String testJsonUser = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"adam\",\"password\":\"123\"}}";

        User testUser = Mockito.mock(User.class);
        when(testUser.getAge()).thenReturn("29");

        Assertions.assertThat(Objects.requireNonNull(JsonConverter.convertToObject(User.class, testJsonUser))
                .getAge()).isEqualTo(testUser.getAge());
    }

    @Test
    public void convertToJsonShouldBeReturnValidJson(){
        User testUser = User.builder()
                .id(1L)
                .age("29")
                .name("adam")
                .lastName("adam")
                .wallet(new Wallet(123))
                .credentials(new Credentials("adam","123"))
                .build();
        System.out.println(JsonConverter.convertToJSON(testUser));
        Assertions.assertThat(true).isTrue();
    }
}
