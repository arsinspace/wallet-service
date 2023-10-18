package ru.ylab.services.proxy;

import ru.ylab.logs.UserActionsAuditor;
import ru.ylab.model.User;
import ru.ylab.services.TransactionalService;
import ru.ylab.services.impl.TransactionalServiceImpl;

/**
 * @implNote The implementation is a Proxy class over the TransactionalServiceImpl and is used to save user
 * actions into application memory before calling methods of the TransactionalServiceImpl
 */
public class TransactionalServiceProxy implements TransactionalService {

    private final TransactionalService transactionalService = new TransactionalServiceImpl();

    @Override
    public boolean processDebitTransaction(String jsonTransaction, User appUser) {
        boolean isSuccess = transactionalService.processDebitTransaction(jsonTransaction, appUser);
        if (isSuccess) {
            UserActionsAuditor.writeAction("Transaction - " + jsonTransaction + " success for user:" + "\n"
            + appUser);
            return true;
        } else {
            UserActionsAuditor.writeAction("Transaction - " + jsonTransaction + " failed for user:" + "\n"
                    + appUser);
            return false;
        }
    }

    @Override
    public boolean processCreditTransaction(String jsonTransaction, User appUser) {
        boolean isSuccess = transactionalService.processCreditTransaction(jsonTransaction, appUser);
        if (isSuccess) {
            UserActionsAuditor.writeAction("Transaction - " + jsonTransaction + " success for user:" + "\n"
                    + appUser);
            return true;
        } else {
            UserActionsAuditor.writeAction("Transaction - " + jsonTransaction + " failed for user:" + "\n"
                    + appUser);
            return false;
        }
    }
}
