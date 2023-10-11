package ru.ylab.services.impl;

import ru.ylab.applicationMemory.TransactionMemory;
import ru.ylab.model.Transactional;
import ru.ylab.model.User;
import ru.ylab.model.enums.TransactionStatus;
import ru.ylab.repository.TransactionalRepository;
import ru.ylab.services.TransactionalService;
import ru.ylab.tools.JsonConverter;

import java.util.Date;

/**
 * This implementation contains the logic for working with transactions
 */
public class TransactionalServiceImpl implements TransactionalService {
    /**
     * Field contains a link to the object TransactionMemory
     */
    private final TransactionalRepository transactionMemory = new TransactionMemory();

    @Override
    public boolean processDebitTransaction(String jsonTransaction, User appUser) {
        Transactional transactional = JsonConverter.convertToObject(Transactional.class,jsonTransaction);
        if (transactional == null) {
            System.out.println("No valid JSON, try again");
            return false;
        }
        if (!checkTransaction(transactional)){
            transactionMemory.saveTransactional(transactional);
            double currentBalance = appUser.getWallet().getBalance() - transactional.getAmount();
            if (currentBalance >= 0){
                appUser.getWallet().setBalance(currentBalance);
                transactional.setTransactionalTime(new Date());
                transactional.setUserId(appUser.getId());
                transactional.setStatus(TransactionStatus.SUCCESSFUL);
                appUser.getTransactionalHistory().add(transactional);
                return true;
            }
            else {
                transactional.setTransactionalTime(new Date());
                transactional.setUserId(appUser.getId());
                transactional.setStatus(TransactionStatus.FAILED);
                appUser.getTransactionalHistory().add(transactional);
                return false;
            }
        } else return false;
    }

    @Override
    public boolean processCreditTransaction(String jsonTransaction, User appUser) {
        Transactional transactional = JsonConverter.convertToObject(Transactional.class,jsonTransaction);
        if (transactional == null) {
            System.out.println("No valid JSON, try again");
            return false;
        }
        if (!checkTransaction(transactional)) {
            transactionMemory.saveTransactional(transactional);
            double currentBalance = appUser.getWallet().getBalance() + transactional.getAmount();
            appUser.getWallet().setBalance(currentBalance);
            transactional.setTransactionalTime(new Date());
            transactional.setUserId(appUser.getId());
            transactional.setStatus(TransactionStatus.SUCCESSFUL);
            appUser.getTransactionalHistory().add(transactional);
            return true;
        } else {
            transactional.setTransactionalTime(new Date());
            transactional.setUserId(appUser.getId());
            transactional.setStatus(TransactionStatus.FAILED);
            appUser.getTransactionalHistory().add(transactional);
            return false;
        }
    }

    /**
     * Transaction validity check
     * @param transactional transaction entity
     * @return boolean result of checking
     */
    private boolean checkTransaction(Transactional transactional){
        return transactionMemory.anyMatchTransactionalById(transactional.getTransactionalId());
    }
}
