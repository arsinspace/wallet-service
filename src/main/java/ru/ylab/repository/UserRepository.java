package ru.ylab.repository;

import ru.ylab.model.Credentials;
import ru.ylab.model.User;

import java.util.List;
import java.util.UUID;
/**
 * Basic interface that provides operations for operating a user entity in application memory
 */
public interface UserRepository {
    /**
     * Find user by user ID in application memory
     * @param id user ID
     * @return user entity
     */
    User findUserById(UUID id);

    /**
     * Save user in application memory
     * @param user user entity
     * @return user entity
     */
    User saveUser(User user);

    /**
     * Update user in application memory
     * @param user user entity
     * @return user entity
     */
    User updateUser(User user);

    /**
     * Delete user from application memory
     * @param user user entity
     * @return user entity
     */
    User deleteUser(User user);

    /**
     * Find user by credentials in application memory
     * @param credentials user entity
     * @return user entity
     */
    User findUserByCredentials(Credentials credentials);

    /**
     * Find all users in application memory. Intended for administrator.
     * @return List of Users
     */
    List<User> findAllUsers();
}
