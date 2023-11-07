package controllersTest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.ylab.walletservice.controller.AuthController;
import ru.ylab.walletservice.in.AuthRequest;
import ru.ylab.walletservice.services.SignupService;
import ru.ylab.walletservice.utils.mappers.JsonConverter;
import ru.ylab.walletservice.utils.security.JWTTokenProvider;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private JWTTokenProvider jwtTokenProvider = mock(JWTTokenProvider.class);
    private SignupService signupService = mock(SignupService.class);

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new AuthController(authenticationManager,jwtTokenProvider,signupService)).build();
    }

    @Test
    @SneakyThrows
    public void authControllerShouldReturnStatusOkTest(){
        mockMvc.perform(
                post("/auth/login")
                        .contentType("application/json")
                        .content(JsonConverter.convertToJSON(new AuthRequest("adam","123"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    public void authControllerShouldReturnStatusUnsupportedTest(){
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.ALL)
                                .content("123"))
                .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    }
    @Test
    @SneakyThrows
    public void authControllerShouldReturnStatusNotFoundTest(){
        mockMvc.perform(
                        post("/login")
                                .contentType("application/json")
                                .content(JsonConverter.convertToJSON(new AuthRequest("adam","123"))))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
