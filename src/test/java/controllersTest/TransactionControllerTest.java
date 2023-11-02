package controllersTest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.walletservice.controller.TransactionsController;
import ru.ylab.walletservice.in.AuthRequest;
import ru.ylab.walletservice.services.TransactionService;
import ru.ylab.walletservice.utils.mappers.JsonConverter;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class TransactionControllerTest {
    private MockMvc mockMvc;
    private TransactionService service = mock(TransactionService.class);

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new TransactionsController(service)).build();
    }
    @Test
    @SneakyThrows
    public void transactionControllerShouldReturnStatusOkTest(){
        mockMvc.perform(
                        post("/transaction/credit")
                                .contentType("application/json")
                                .requestAttr("userId",1L)
                                .content("{\"transactionalId\":\"yourId\",\"purpose\":\"example\",\"amount\":123}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    public void transactionControllerShouldReturnStatusUnsupportedTest(){
        mockMvc.perform(
                        post("/transaction/credit")
                                .contentType(MediaType.ALL)
                                .content("123"))
                .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }
    @Test
    @SneakyThrows
    public void transactionControllerShouldReturnStatusNotFoundTest(){
        mockMvc.perform(
                        post("/credit")
                                .contentType("application/json")
                                .content(JsonConverter.convertToJSON(new AuthRequest("adam","123"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
