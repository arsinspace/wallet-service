import org.junit.jupiter.api.Test;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.annotation.TrackEvent;
import ru.ylab.walletservice.utils.annotation.TrackParameter;

/**
 * Test aspects
 */
public class UserActionAspectTest {

    @Test
    public void aspectTest(){
        trackTestMethod("2");
    }
    @Loggable
    @TrackEvent(action = "testTrack")
    public void trackTestMethod(@TrackParameter("parameter") String parameter){
        System.out.println(1 + " " + parameter);
    }
}
