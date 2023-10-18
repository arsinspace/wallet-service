package ru.ylab;

import ru.ylab.controller.UserController;
import ru.ylab.dao.TransactionDAO;
import ru.ylab.dao.UserDAO;
import ru.ylab.services.impl.TransactionalServiceImpl;
import ru.ylab.services.impl.UserServiceImpl;
import ru.ylab.services.proxy.TransactionalServiceProxy;
import ru.ylab.services.proxy.UserServiceProxy;

/**
 * @author arsinspace
 * @version 0.0.X SuperAgent
 */
public class WalletServiceApplication {
    /**
     * Start point of the application
     * @param args command line values
     */
    public static void main(String[] args) {



        System.out.println("""
                Welcome to Wallet Service Application!
                ============= Version 0.0.X SuperAgent =============
                """);

        UserController controller = new UserController(new UserServiceProxy
                (new UserServiceImpl(new UserDAO())),new TransactionalServiceProxy
                (new TransactionalServiceImpl(new TransactionDAO())));
        controller.receiveCommand();
    }
}