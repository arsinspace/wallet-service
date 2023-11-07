package ru.ylab.walletservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.walletservice.dao.WalletDAO;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.util.Collections;

/**
 * This controller contains endpoint for view user funds
 */
@RestController("/wallet")
@RequiredArgsConstructor
@Tag(name = "Wallet", description = "Endpoint for view user funds")
public class WalletController {
    /**
     * This field contains link to WalletDAO
     */
    private final WalletDAO walletDAO;

    /**
     * Method for user, view his wallet funds
     * @param userId Long
     * @return Returns wallet funds of user
     */
    @TrackEvent(action = "User look his wallet funds")
    @GetMapping(produces = {"application/json"})
    @Operation(summary = "Returns wallet funds of logged in user")
    @ApiResponse(responseCode = "200", description = "{Balance: }")
    @ApiResponse(responseCode = "400", description = "{message: }")
    public ResponseEntity<Object> getBalance(@RequestBody long userId) {
        try {
            int balance = walletDAO.findWalletByUserId(userId);
            return ResponseEntity.ok().body(Collections.singletonMap("Balance:", balance));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e));
        }
    }
}
