package ru.ylab.walletservice.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dto.TransactionDTO;
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
     * @param userId Long userId
     * @param transactionDTO TransactionDTO
     * @return String result of method
     */
    String processDebitTransaction(TransactionDTO transactionDTO, long userId);

    /**
     * The method converts a json object into a transaction object, checks the presence
     * of a transaction id in the application memory, checks the possibility of
     * credit funds from the user's wallet, save the transaction entity in application memory,
     * credit funds to the user's wallet
     * @param userId Long userId
     * @param transactionDTO TransactionDTO
     * @return String result of method
     */
    String  processCreditTransaction(TransactionDTO transactionDTO, long userId);

    /**
     * Method find all transactions by UserId in database
     * @param userId Long userId
     * @return List of transactions
     */
    List<Transaction> getAllTransactions(long userId);
}
