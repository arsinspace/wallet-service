package controllersTest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.walletservice.controller.AdminController;
import ru.ylab.walletservice.dao.UserActionDAO;
import ru.ylab.walletservice.in.AuthRequest;
import ru.ylab.walletservice.utils.mappers.JsonConverter;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AdminControllerTest {

    private MockMvc mockMvc;
    private UserActionDAO userActionDAO = mock(UserActionDAO.class);

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new AdminController(userActionDAO)).build();
    }

    @Test
    @SneakyThrows
    public void adminControllerShouldReturnStatusOkTest(){
        mockMvc.perform(
                        get("/user-actions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    public void adminControllerShouldReturnStatusNotFoundTest(){
        mockMvc.perform(
                        get("/admin/user-actions"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
