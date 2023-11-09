package ru.ylab.walletservice.services.impl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ylab.walletservice.dao.UserDAO;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.repository.CredentialsRepository;
import ru.ylab.walletservice.services.SignupService;

import ru.ylab.walletservice.utils.validatos.UserValidator;

/**
 * Class for converts the json object into a user object, saves the user in
 * database and assigns the class field a new value of the specified user
 */
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {
    /**
     * This field contains link to UserDAO
     */
    private final UserDAO userDAO;
    /**
     * This field contains link to PasswordEncoder
     */
    private final PasswordEncoder encoder;
    /**
     * This field contains link to CredentialsRepository
     */
    private final CredentialsRepository credentialsRepository;

    @Override
    public String processSignup(User user){

        try {
            UserValidator.getInstance().isValid(user);
        } catch (ValidationException e){
            return "Not valid User " + e;
        }
        if (!credentialsRepository.isLoginUsed(user.getCredentials().getLogin())){
            user.getCredentials().setPassword(encoder.encode(user.getPassword()));
            long userId = userDAO.saveUser(user);
            return "Success signup";
        } else {
            return  "Login is already used";
        }
    }
}
