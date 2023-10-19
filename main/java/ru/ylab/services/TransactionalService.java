package ru.ylab.services;

import ru.ylab.model.User;

/**
 * Basic interface providing operations for working with transactions
 */
public interface TransactionalService {
    /**
     * The method converts a json object into a transaction object, checks the presence
     * of a transaction id in the application memory, checks the possibility of
     * debit funds from the user's wallet, save the transaction entity in application memory,
     * debit funds from the user's wallet
     * @param jsonTransaction JSON format String
     * @param appUser current application user
     * @return boolean result of method
     */
    boolean processDebitTransaction(String jsonTransaction,User appUser);

    /**
     * The method converts a json object into a transaction object, checks the presence
     * of a transaction id in the application memory, checks the possibility of
     * credit funds from the user's wallet, save the transaction entity in application memory,
     * credit funds to the user's wallet
     * @param jsonTransaction JSON format String
     * @param appUser current application user
     * @return boolean result of method
     */
    boolean processCreditTransaction(String jsonTransaction,User appUser);

    void getAllTransactions(long userId);
}
