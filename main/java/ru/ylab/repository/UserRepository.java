package ru.ylab.repository;

import ru.ylab.model.Credentials;
import ru.ylab.model.User;

import java.util.List;
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
     * Find user by credentials in database
     *
     * @param credentials user entity
     * @return Optional user entity
     */
    Optional<User> findUserByCredentials(Credentials credentials);

    /**
     * Find all users in database. Intended for administrator.
     * @return List of Users
     */
    List<User> findAllUsers();
}
