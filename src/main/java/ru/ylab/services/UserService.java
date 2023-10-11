package ru.ylab.services;

import ru.ylab.model.User;

/**
 * Basic interface providing operations for working with users
 */
public interface UserService {
    /**
     * Method converts the json object into a user object, saves the user in
     * application memory and assigns the class field a new value of the specified user
     * @param userJson JSON type String
     * @return user entity
     */
    User processRegistration(String userJson);

    /**
     * The method converts a JSON object into a credentials object and checks whether users
     * with the passed credentials exist in the application memory, assigns the class field a
     * new value of the specified user. If the user has entered administrator
     * credentials, then the user is assigned as administrator
     * @param credentialsJson JSON type String
     * @return boolean result of method
     */
    boolean processLogin(String credentialsJson);

    /**
     * Get the current user in userService
     * @return user object
     */
    User getCurrentAppUser();

    /**
     * Assigns a field with a current user value null
     * @return boolean result of method
     */
    boolean processLogout();

    /**
     * Update user in application memory
     * @param user user object
     */
    void updateUser(User user);

    /**
     * Response contains all current user transactions
     */
    void getUserTransactionalHistory();

    /**
     * Response contains current user balance
     */
    void getUserWallet();

    /**
     * Response contains all users in application memory.
     * Intended for admin
     */
    void processAdminPanelViewAllUsers();

    /**
     * Response contains all actions of users in UserActionsAuditor memory.
     * Intended for admin
     */
    void processAdminPanelViewUserActionsAuditor();
}
