package ru.ylab.walletservice.repository;

/**
 * Basic interface that provides operations for operating a user credentials entity in Database
 */
public interface CredentialsRepository {

    /**
     * This field contains sql query for find user credentials entity in DB
     */
    String FIND_CREDENTIAL_BY_USERNAME = "select * from wallet.credentials where username = ?";

    /**
     * This method checking is login used in Database
     *
     * @param userName String users username
     * @return boolean result of checking
     */
    boolean isLoginUsed(String userName);
}
