package controllersTest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.walletservice.controller.WalletController;
import ru.ylab.walletservice.dao.WalletDAO;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class WalletControllerTest {

    private MockMvc mockMvc;
    private WalletDAO walletDAO = mock(WalletDAO.class);

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new WalletController(walletDAO)).build();
    }

    @Test
    @SneakyThrows
    public void adminControllerShouldReturnStatusOkTest(){
        mockMvc.perform(
                        get("/wallet"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    public void adminControllerShouldReturnStatusNotFoundTest(){
        mockMvc.perform(
                        get("/wallet/wallet"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
