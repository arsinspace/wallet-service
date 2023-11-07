package ru.ylab.walletservice.utils.validatos;

import jakarta.validation.ValidationException;
import ru.ylab.walletservice.model.Credentials;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.model.Wallet;

import java.util.Objects;
import java.util.function.Predicate;
/**
 * Utility class for validate User entity
 */
public class UserValidator {

    private static final UserValidator USER_VALIDATOR = new UserValidator();

    public static UserValidator getInstance(){
        return USER_VALIDATOR;
    }

    private final Predicate<Long> idMustBeNull = id -> Objects.isNull(id) || id == 0;
    private final Predicate<String> namePredicate = name -> name.length() <= 15 && Objects.nonNull(name);
    private final Predicate<String> lastNamePredicate = lastname -> lastname.length() <= 15 &&
            Objects.nonNull(lastname);
    private final Predicate<String> agePredicate = age -> Integer.parseInt(age) > 0 && Objects.nonNull(age);
    private final Predicate<Wallet> walletPredicate = wallet -> Objects.isNull(wallet);
    private final Predicate<Credentials> credentialsPredicate = credentials -> Objects.nonNull(credentials)
            && credentials.getPassword().length() >= 3 && credentials.getLogin().length() <= 15;

    public boolean isValid(User user) {

        if (!idMustBeNull.test(user.getId())){
            System.out.println(user.getId());
            throw new ValidationException("User id must be NULL");
        } else if (!namePredicate.test(user.getName())){
            throw new ValidationException("Invalid User name");
        } else if (!lastNamePredicate.test(user.getLastName())){
            throw new ValidationException("Invalid User lastName");
        } else if (!agePredicate.test(user.getAge())){
            throw new ValidationException("Invalid User age");
        } else if (!walletPredicate.test(user.getWallet())) {
            throw new ValidationException("Wallet must be NULL");
        } else if (!credentialsPredicate.test(user.getCredentials())){
            throw new ValidationException("Invalid Credentials");
        }
        return true;
    }
}
