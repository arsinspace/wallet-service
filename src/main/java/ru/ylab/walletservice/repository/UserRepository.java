package ru.ylab.walletservice.repository;

import ru.ylab.walletservice.model.User;

import java.util.Optional;
/**
 * Basic interface that provides operations for operating a user entity in database
 */
public interface UserRepository {

    /**
     * Save user in database
     * @param user user entity
     * @return user entity
     */
    long saveUser(User user);

    /**
     * Find User entity in database by username
     * @param username String
     * @return Optional User entity
     */
    Optional<User> findByUsername(String username);

    /**
     * Find userId in database by username
     * @param username String
     * @return Optional long userId
     */
    Optional<Long> findUserIdByUsername(String username);
}
