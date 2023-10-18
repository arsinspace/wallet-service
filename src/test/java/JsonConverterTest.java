import org.junit.Assert;
import org.junit.Test;
import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.utils.JsonConverter;

/**
 * Tests for JsonConverter
 */
public class JsonConverterTest {

    @Test
    public void convertToObjectShouldBeReturnValidObject(){
        String testJsonUser = "{\"name\":\"Adam\",\"lastName\":\"Adam\",\"age\":\"29\",\"credentials\"" +
                ":{\"login\":\"adam\",\"password\":\"123\"}}";
        User testUser = User.builder()
                .name("Adam")
                .lastName("Adam")
                .age(String.valueOf(29))
                .credentials(new Credentials("adam","123"))
                .build();

        Assert.assertEquals(JsonConverter.convertToObject(User.class,testJsonUser).getCredentials().toString(),
                testUser.getCredentials().toString());
    }
}
