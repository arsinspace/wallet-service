package ru.ylab.services.proxy;

import ru.ylab.model.User;
import ru.ylab.repository.UserActionRepository;
import ru.ylab.services.TransactionalService;

import java.sql.Timestamp;

/**
 * @implNote The implementation is a Proxy class over the TransactionalServiceImpl and is used to save user
 * actions into application memory before calling methods of the TransactionalServiceImpl
 */
public class TransactionalServiceProxy implements TransactionalService {

    private final TransactionalService transactionalService;

    public TransactionalServiceProxy(TransactionalService transactionalService) {

        this.transactionalService = transactionalService;
    }


    @Override
    public boolean processDebitTransaction(String jsonTransaction, User appUser) {
        boolean isSuccess = transactionalService.processDebitTransaction(jsonTransaction, appUser);
        if (isSuccess) {
            UserActionRepository.saveUserAction(appUser.getId(),
                    "user id: " + appUser.getId() + " - debit transaction",
                    "success",
                    new Timestamp(System.currentTimeMillis()));
            return true;
        } else {
            UserActionRepository.saveUserAction(appUser.getId(),
                    "user id: " + appUser.getId() + " - debit transaction",
                    "failed",
                    new Timestamp(System.currentTimeMillis()));
            return false;
        }
    }

    @Override
    public boolean processCreditTransaction(String jsonTransaction, User appUser) {
        boolean isSuccess = transactionalService.processCreditTransaction(jsonTransaction, appUser);
        if (isSuccess) {
            UserActionRepository.saveUserAction(appUser.getId(),
                    "user id: " + appUser.getId() + " - credit transaction",
                    "success",
                    new Timestamp(System.currentTimeMillis()));
            return true;
        } else {
            UserActionRepository.saveUserAction(appUser.getId(),
                    "user id: " + appUser.getId() + " - credit transaction",
                    "failed",
                    new Timestamp(System.currentTimeMillis()));
            return false;
        }
    }

    @Override
    public void getAllTransactions(long userId) {
        UserActionRepository.saveUserAction(userId,
                "user id: " + userId + " - look his transactional history",
                "success",
                new Timestamp(System.currentTimeMillis()));
        transactionalService.getAllTransactions(userId);
    }
}
