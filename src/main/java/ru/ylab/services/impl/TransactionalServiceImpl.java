package ru.ylab.services.impl;

import ru.ylab.model.Transactional;
import ru.ylab.model.User;
import ru.ylab.model.enums.TransactionStatus;
import ru.ylab.repository.TransactionalRepository;
import ru.ylab.services.TransactionalService;
import ru.ylab.utils.JsonConverter;

import java.sql.Timestamp;

/**
 * This implementation contains the logic for working with transactions
 */
public class TransactionalServiceImpl implements TransactionalService {
    /**
     * Field contains a link to the object TransactionMemory
     */
    private final TransactionalRepository transactionDAO;

    public TransactionalServiceImpl(TransactionalRepository transactionDAO) {
        this.transactionDAO = transactionDAO;
    }


    @Override
    public boolean processDebitTransaction(String jsonTransaction, User appUser) {
        Transactional transaction = JsonConverter.convertToObject(Transactional.class,jsonTransaction);
        if (transaction == null) {
            System.out.println("No valid JSON, try again");
            return false;
        }
        if (!checkTransaction(transaction)){
            int currentBalance = appUser.getWallet().getBalance() - transaction.getAmount();
            if (currentBalance >= 0){
                appUser.getWallet().setBalance(currentBalance);
                transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
                transaction.setUserId(appUser.getId());
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                transactionDAO.saveTransaction(transaction);
                return true;
            }
            else {
                transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
                transaction.setUserId(appUser.getId());
                transaction.setStatus(TransactionStatus.FAILED);
                transactionDAO.saveTransaction(transaction);
                return false;
            }
        } else{
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(appUser.getId());
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.saveTransaction(transaction);
            return false;
        }
    }

    @Override
    public boolean processCreditTransaction(String jsonTransaction, User appUser) {
        Transactional transaction = JsonConverter.convertToObject(Transactional.class,jsonTransaction);
        if (transaction == null) {
            System.out.println("No valid JSON, try again");
            return false;
        }
        if (!checkTransaction(transaction)) {
            int currentBalance = appUser.getWallet().getBalance() + transaction.getAmount();
            appUser.getWallet().setBalance(currentBalance);
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(appUser.getId());
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transactionDAO.saveTransaction(transaction);
            return true;
        } else {
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(appUser.getId());
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.saveTransaction(transaction);
            return false;
        }
    }

    @Override
    public void getAllTransactions(long userId) {
            transactionDAO.findAllByUserId(userId).forEach(System.out::println);

    }
    /**
     * Transaction validity check
     * @param transaction transaction entity
     * @return boolean result of checking
     */
    private boolean checkTransaction(Transactional transaction){
            return transactionDAO.anyMatchTransactionalById(transaction.getTransactionalId());
    }
}
