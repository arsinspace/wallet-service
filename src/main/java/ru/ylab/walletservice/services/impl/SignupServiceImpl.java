package ru.ylab.walletservice.services.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.lang.JoseException;
import ru.ylab.walletservice.dao.UserDAO;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.repository.CredentialsRepository;
import ru.ylab.walletservice.services.SignupService;
import ru.ylab.walletservice.utils.JWTGenerator;
import ru.ylab.walletservice.utils.JsonConverter;
import ru.ylab.walletservice.utils.validatos.UserValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for converts the json object into a user object, saves the user in
 * database and assigns the class field a new value of the specified user
 */
public class SignupServiceImpl implements SignupService {

    private final UserDAO userDAO;

    public SignupServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void processSignup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = JsonConverter.getMapper().readValue(req.getInputStream(),User.class);

        try {
            UserValidator.getInstance().isValid(user);
        } catch (ValidationException e){
            SendResponse.getInstance().sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

        if (!CredentialsRepository.isLoginUsed(user.getCredentials().getLogin())){

            long userId = userDAO.saveUser(user);
            String userName = user.getCredentials().getLogin();
            String jwt = null;
            try {
                jwt = setJwt(userName);
            } catch (JoseException e) {
                SendResponse.getInstance().sendResponse(resp,
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }

            Cookie jwtCookie = new Cookie("jwt", jwt);
            Cookie userIdCookie = new Cookie("userId",String.valueOf(userId));
            resp.addCookie(jwtCookie);
            resp.addCookie(userIdCookie);

            SendResponse.getInstance().sendResponse(resp, HttpServletResponse.SC_CREATED, "Success signup");

        } else {
            SendResponse.getInstance().sendResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST, "Login is already used");
        }
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
