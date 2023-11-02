package ru.ylab.walletservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ru.ylab.walletservice.in.AuthRequest;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.services.SignupService;
import ru.ylab.walletservice.utils.annotation.TrackEvent;
import ru.ylab.walletservice.utils.security.JWTTokenProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
/**
 * This controller contains logic for endpoint POST /login and POST /signup
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and token management")
public class AuthController {
    /**
     * This field contains link to AuthenticationManager
     */
    private final AuthenticationManager authenticationManager;
    /**
     * This field contains link to JWTTokenProvider
     */
    private final JWTTokenProvider jwtTokenProvider;
    /**
     * This field contains link to SignupService
     */
    private final SignupService signupService;

    /**
     * Method contains logic for logs in user into the application
     * @param data AuthRequest
     * @return Returns access-token
     */
    @TrackEvent(action = "User login")
    @PostMapping(value = "/login", consumes = {"application/json"})
    @Operation(summary = "Logs in user into the application", description = "Returns access-token on " +
            "successful authentication which provides access to protected endpoints")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{username: token: }"),
            @ApiResponse(responseCode = "400", description = "Bad credentials. Invalid username/password supplied") })
    public ResponseEntity<Object> login(@RequestBody AuthRequest data) {
        try {
            String username = data.getUsername();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(authentication);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().
                    body(Collections.singletonMap("message", "Bad credentials. Invalid username/password supplied"));
        }
    }

    /**
     * Method contains logic for signup user into the application
     * @param user
     * @return Json message
     */
    @TrackEvent(action = "User signup")
    @PostMapping(value = "/signup", consumes = ("application/json"))
    @Operation(summary = "Signup user into the application", description = "Returns json message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{message: Success signup}"),
            @ApiResponse(responseCode = "433", description = "{message: Login is already used}"),
            @ApiResponse(responseCode = "400", description = "{message: }") })
    public ResponseEntity<Object> signup (@RequestBody User user) {

        String response = signupService.processSignup(user);

        switch (response) {
            case "Success signup" -> {
                return ok(Collections.singletonMap("message", "Success signup"));
            }
            case "Login is al   ready used" -> {
                return ResponseEntity.unprocessableEntity().
                        body(Collections.singletonMap("message", "Login is already used"));
            }
            default -> {
                return ResponseEntity.unprocessableEntity().body(Collections.singletonMap("message",response));
            }
        }
    }
}
