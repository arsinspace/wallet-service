package ru.ylab.walletservice.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.model.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * Basic interface providing operations for working with transactions
 */
public interface TransactionService {
    /**
     * The method converts a json object into a transaction object, checks the presence
     * of a transaction id in the application memory, checks the possibility of
     * debit funds from the user's wallet, save the transaction entity in application memory,
     * debit funds from the user's wallet
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param userId current application userId
     * @return boolean result of method
     */
    boolean processDebitTransaction(HttpServletRequest request, HttpServletResponse response, long userId)
            throws IOException;

    /**
     * The method converts a json object into a transaction object, checks the presence
     * of a transaction id in the application memory, checks the possibility of
     * credit funds from the user's wallet, save the transaction entity in application memory,
     * credit funds to the user's wallet
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param userId current application userId
     * @return boolean result of method
     */
    boolean processCreditTransaction(HttpServletRequest request, HttpServletResponse response, long userId) throws IOException;

    /**
     * Method find all transactions by UserId in database
     * @param userId long appUserId
     * @return List of transactions
     */
    List<Transaction> getAllTransactions(long userId);
}
