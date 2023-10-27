package ru.ylab.walletservice.services.impl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.lang.JoseException;
import ru.ylab.walletservice.dao.UserDAO;
import ru.ylab.walletservice.model.Credentials;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.services.LoginService;
import ru.ylab.walletservice.utils.JWTGenerator;
import ru.ylab.walletservice.utils.JsonConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
/**
 * This service converts a JSON object into a credentials object and checks whether users
 * with the passed credentials exist in the database, assigns the class field a
 * new value of the specified user. If the user has entered administrator
 * credentials, then the user is assigned as administrator
 */
public class LoginServiceImpl implements LoginService {
    /**
     * This field contains link to UserDAO
     */
    private final UserDAO userDAO;

    public LoginServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void processLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Credentials credentials = null;

        try {
            credentials = JsonConverter.getMapper().readValue(req.getInputStream(), Credentials.class);
        } catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getOutputStream().write(JsonConverter.getMapper()
                    .writeValueAsBytes(Collections.singletonMap("message",e)));
        }

        if (credentials.getLogin().equals("admin") && credentials.getPassword().equals("admin")){
            processLoginAdmin(resp);
        } else {
            String userName = credentials.getLogin();
            Optional<User> appUser = userDAO.findUserByCredentials(credentials);

            if (appUser.isPresent()){
                String jwt = null;
                try {
                    jwt = setJwt(userName);
                } catch (JoseException e) {
                    SendResponse.getInstance().sendResponse(resp,
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.getMessage());
                }
                Cookie jwtCookie = new Cookie("jwt", jwt);
                Cookie userIdCookie = new Cookie("userId", String.valueOf(appUser.get().getId()));
                resp.addCookie(jwtCookie);
                resp.addCookie(userIdCookie);
                SendResponse.getInstance().sendResponse(resp,HttpServletResponse.SC_FOUND,"Success login");
            } else {
                SendResponse.getInstance().sendResponse(resp,HttpServletResponse.SC_NOT_FOUND,"User not found");
            }
        }
    }

    /**
     * Logic for admin credentials
     * @param resp HttpServletResponse
     * @throws IOException
     */
    private void processLoginAdmin(HttpServletResponse resp) throws IOException {
        Cookie adminCookie = new Cookie("admin", "adminUser");
        resp.addCookie(adminCookie);
        SendResponse.getInstance().sendResponse(resp,HttpServletResponse.SC_FOUND,"Success login ad Admin");
    }

    /**
     * Set JWT to User
     * @param name String
     * @return String JWTSignature
     * @throws JoseException
     */
    private static String setJwt(String name) throws JoseException {
        RsaJsonWebKey rsaJsonWebKey = null;
        try {
            rsaJsonWebKey = JWTGenerator.getInstance().getRsaJsonWebKey();
        } catch (JoseException e) {
            throw new JoseException(e.getMessage());
        }
        List<String> roles = new ArrayList<>();
        roles.add("default");
        JwtClaims jwtClaims = JWTGenerator.getInstance().getJwtClaims(name, roles);
        String jwtSignature = null;
        try {
            jwtSignature = JWTGenerator.getInstance().getJwtSignature(rsaJsonWebKey, jwtClaims);
        } catch (JoseException e) {
            throw new JoseException(e.getMessage());
        }
        JwtConsumer jwtConsumer = JWTGenerator.getInstance().getJwtConsumer(rsaJsonWebKey);
        try {
            JWTGenerator.getInstance().validate(jwtSignature, jwtConsumer);
        } catch (MalformedClaimException e) {
            throw new JoseException(e.getMessage());
        }
        return jwtSignature;
    }
}
