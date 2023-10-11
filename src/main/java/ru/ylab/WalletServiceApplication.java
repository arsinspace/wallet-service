package ru.ylab;

import ru.ylab.controller.UserController;

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

        UserController controller = new UserController();
        controller.receiveCommand();
    }
}