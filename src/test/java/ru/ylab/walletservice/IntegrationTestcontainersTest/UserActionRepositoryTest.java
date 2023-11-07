package IntegrationTestcontainersTest;

import config.ContainerEnvironment;
import org.junit.Assert;
import org.junit.Test;
import ru.ylab.model.UserAction;
import ru.ylab.repository.UserActionRepository;

import java.sql.Timestamp;

public class UserActionRepositoryTest extends ContainerEnvironment {

    @Test
    public void when_saveUserAction_expect_true() {
        UserAction action = UserAction.builder()
                .userId(1L)
                .status("ok")
                .action("test action")
                .time(new Timestamp(System.currentTimeMillis()))
                .build();
        UserActionRepository.saveUserAction(action.getUserId(),action.getAction(),action.getStatus(),action.getTime());
    }

    @Test
    public void when_findAllUserActions_expect_non_empty_list() {
        Assert.assertFalse(UserActionRepository.findAllUserActions().isEmpty());
    }

}
