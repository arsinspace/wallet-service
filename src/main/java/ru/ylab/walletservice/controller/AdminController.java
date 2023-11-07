package ru.ylab.walletservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ylab.walletservice.dao.UserActionDAO;
import ru.ylab.walletservice.model.UserAction;
import ru.ylab.walletservice.utils.annotation.Loggable;

import java.util.Collections;
import java.util.List;
/**
* This controller contains logic for endpoints GET /user-actions
*/
@RestController("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Endpoints for administrator")
public class AdminController {
    /**
     * Filed contains link to UserActionDAO
     */
    private final UserActionDAO userActionDAO;

    @Loggable
    @GetMapping(value = "/user-actions", produces = {"application/json"})
    @Operation(summary = "Returns list of all user actions", description = "View all user actions in application")
    @ApiResponse(responseCode = "200", description = "{user actions list}")
    @ApiResponse(responseCode = "400", description = "{message: }")
    public ResponseEntity<Object> getUserActions() {
        try {
            List<UserAction> userActions = userActionDAO.findAllUserActions();
            return ResponseEntity.ok().body(userActions);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e));
        }
    }
}
