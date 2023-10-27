import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.model.User;
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
}
