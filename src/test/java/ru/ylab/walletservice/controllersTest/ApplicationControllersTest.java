package ru.ylab.walletservice.controllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.ylab.walletservice.config.WebSecurityConfig;
import ru.ylab.walletservice.controller.AuthController;
import ru.ylab.walletservice.controller.TransactionsController;
import ru.ylab.walletservice.controller.WalletController;
import ru.ylab.walletservice.dao.WalletDAO;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.in.AuthRequest;
import ru.ylab.walletservice.model.Credentials;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.model.Wallet;
import ru.ylab.walletservice.repository.TransactionalRepository;
import ru.ylab.walletservice.repository.UserRepository;
import ru.ylab.walletservice.repository.WalletRepository;
import ru.ylab.walletservice.services.SignupService;
import ru.ylab.walletservice.services.TransactionService;
import ru.ylab.walletservice.utils.mappers.JsonConverter;
import ru.ylab.walletservice.utils.security.JWTTokenProvider;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = {WalletController.class,
                            TransactionsController.class, AuthController.class})
@Import({WebSecurityConfig.class})
public class ApplicationControllersTest {

    @MockBean
    WalletRepository walletRepository;

    @MockBean
    WalletDAO walletDAO;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TransactionalRepository transactionalRepository;

    @MockBean
    SignupService signupService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JWTTokenProvider jwtTokenProvider;

    @MockBean
    TransactionService transactionService;

    @BeforeEach
    public void setUp(){

        //setupTransaction
        when(transactionService.getAllTransactions(any(Long.class))).thenReturn(List.of());
        when(transactionService.processCreditTransaction(any(TransactionDTO.class),any(Long.class)))
                .thenReturn("Transaction created");
        when(transactionService.processDebitTransaction(any(TransactionDTO.class),any(Long.class)))
                .thenReturn("Transaction created");
        when(transactionalRepository.anyMatchTransactionalById(any(String.class))).thenReturn(false);

        //setupUser
        when(userRepository.findUserIdByUsername(any(String.class))).thenReturn(Optional.of(1L));
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(User.builder()
                        .id(1L)
                        .age("29")
                        .name("adam")
                        .lastName("adam")
                        .credentials(new Credentials("myusername",
                                "{bcrypt}$2a$10$zxRfwiLQYk/3LZDlPiMymuKCfcecnDr3SPsYanfLsWBBNYFMhW2f2"))
                        .wallet(new Wallet(123))
                .build()));

        //setupAuth


        //setupSecurity
        String scope = "ROLE_USER";
        String testToken = "test-token";
        var accessTokenAuthority = List.<GrantedAuthority>of(new SimpleGrantedAuthority(scope));
        when(jwtTokenProvider.validateToken(testToken)).thenReturn(true);
        when(jwtTokenProvider.getAuthentication(testToken)).thenReturn(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return accessTokenAuthority;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "adam";
            }
        });
        given(this.walletRepository.findWalletByUserId(1L))
                .willReturn(123);
    }

    @Test
    @SneakyThrows
    @DisplayName("This test request GET method to walletController and expect return status is ok")
    public void testWalletControllerGetBalance(){
        this.mockMvc.perform(get("/wallet/get-balance").requestAttr("user_id",1L)
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("This test request GET method to transactionController and expect return status is ok")
    public void testTransactionControllerGetHistory(){
        this.mockMvc.perform(get("/transaction/history").requestAttr("user_id",1L)
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("This test request POST method CREDIT to transaction " +
            "controller and expect return status is ok")
    public void testTransactionControllerPostCreditTransaction(){
        this.mockMvc.perform(post("/transaction/credit")
                .requestAttr("user_id",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.convertToJSON(
                                new TransactionDTO("123","123",1)))
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @DisplayName("This test request POST method DEBIT to transaction " +
            "controller and expect return status is ok")
    public void testTransactionControllerPostDebitTransaction(){
        this.mockMvc.perform(post("/transaction/debit")
                        .requestAttr("user_id",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.convertToJSON(
                                new TransactionDTO("123","123",1)))
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @DisplayName("This test request POST method to auth " +
            "controller and expect return status is ok")
    public void testAuthControllerPostLogin(){
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConverter.convertToJSON(new AuthRequest("test","test"))))
                .andExpect(status().isOk());
    }
}
