package ru.ylab.walletservice.services;

import ru.ylab.walletservice.model.User;

/**
 * Basic interface providing operations for working with signup
 */
public interface SignupService {

    /**
     * Contains logic for working with signup request
     * @param user User entity
     */
    String processSignup(User user);
}
