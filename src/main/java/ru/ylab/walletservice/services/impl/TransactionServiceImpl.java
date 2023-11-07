package ru.ylab.walletservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ylab.walletservice.dao.WalletDAO;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.model.enums.TransactionStatus;
import ru.ylab.walletservice.repository.TransactionalRepository;
import ru.ylab.walletservice.services.TransactionService;
import ru.ylab.walletservice.utils.mappers.TransactionDTOMapper;
import ru.ylab.walletservice.utils.validatos.TransactionDTOValidator;

import java.sql.Timestamp;
import java.util.List;

/**
 * This implementation contains the logic for working with transactions
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    /**
     * Field contains a link to the object TransactionMemory
     */
    private final TransactionalRepository transactionDAO;
    private final WalletDAO walletDAO;


    @Override
    public String processDebitTransaction(TransactionDTO transactionDTO, long userId){

        Transaction transaction;
        int balance = walletDAO.findWalletByUserId(userId);

        try {
            //validation DTO from request
            TransactionDTOValidator.getInstance().isValid(transactionDTO);
            //mapping from dto to Transaction
            transaction = TransactionDTOMapper.INSTANCE.toTransaction(transactionDTO);
        } catch (Exception e){
            return "No valid transaction " + e;
        }

        if (!checkTransaction(transaction)){
            int currentBalance = balance - transaction.getAmount();
            if (currentBalance >= 0){
                walletDAO.updateBalance(userId,currentBalance);
                transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
                transaction.setUserId(userId);
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                transactionDAO.saveTransaction(transaction);
                return "Transaction created";
            }
            else {
                transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
                transaction.setUserId(userId);
                transaction.setStatus(TransactionStatus.FAILED);
                transactionDAO.saveTransaction(transaction);
                return "Not enough funds in the account";
            }
        } else{
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(userId);
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.saveTransaction(transaction);
            return "No valid transaction id";
        }
    }

    @Override
    public String processCreditTransaction(TransactionDTO transactionDTO, long userId){

        Transaction transaction;
        int balance = walletDAO.findWalletByUserId(userId);

        try {
            //validation DTO from request
            TransactionDTOValidator.getInstance().isValid(transactionDTO);
            //mapping from dto to Transaction
            transaction = TransactionDTOMapper.INSTANCE.toTransaction(transactionDTO);
        } catch (Exception e){
            return "No valid transaction " + e;
        }

        if (!checkTransaction(transaction)) {
            int currentBalance = balance + transaction.getAmount();
            walletDAO.updateBalance(userId,currentBalance);
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(userId);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transactionDAO.saveTransaction(transaction);
            return "Transaction created";

        } else {
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(userId);
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.saveTransaction(transaction);
            return "No valid transaction id";
        }
    }

    @Override
    public List<Transaction> getAllTransactions(long userId) {
            return transactionDAO.findAllByUserId(userId);
    }
    /**
     * Transaction validity check
     * @param transaction transaction entity
     * @return boolean result of checking
     */
    private boolean checkTransaction(Transaction transaction){
            return transactionDAO.anyMatchTransactionalById(transaction.getTransactionalId());
    }
}
