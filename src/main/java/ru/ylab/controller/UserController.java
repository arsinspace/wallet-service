package ru.ylab.controller;

import ru.ylab.in.Request;
import ru.ylab.model.User;
import ru.ylab.out.response.ApplicationResponse;
import ru.ylab.out.response.impl.*;
import ru.ylab.services.TransactionalService;
import ru.ylab.services.UserService;
import ru.ylab.services.proxy.TransactionalServiceProxy;
import ru.ylab.services.proxy.UserServiceProxy;

/**
 * Contains endpoints and logic for working with requests
 */
public class UserController {
    /**
     * Field with UserServiceProxy
     */
    private final UserService userService = new UserServiceProxy();
    /**
     * Field with TransactionalServiceProxy
     */
    private final TransactionalService transactionalService = new TransactionalServiceProxy();
    /**
     * Field with Request
     */
    private final Request userRequest = new Request();

    /**
     * Receive command and transfer request to the required methods, also calls receiveCommand() method
     * and give response
     */
    public void receiveCommand(){
        if (userService.getCurrentAppUser() == null){
            response(new HelpMessageResponse());
            String request = this.userRequest.getRequest();
            if (isCommand(request)){
                switch (request){
                    case "/register" -> registration();
                    case "/login" -> login();
                    case "/exit" -> exit();
                    default -> receiveCommand();
                }
            }
            else {
                response(new HelpMessageResponse());
                receiveCommand();
            }
        }
        else if (userService.getCurrentAppUser().getCredentials().getLogin().equals("admin")){
            adminPanel();
        }
        else {
            responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.WORK_WITH_ACCOUNT_HELP);
            String request = this.userRequest.getRequest();
            if (isCommand(request)){
                switch (request){
                    case "/transaction" -> transaction();
                    case "/wallet" -> wallet();
                    case "/history" -> history();
                    case "/logout" -> logout();
                    default -> receiveCommand();
                }
            } else {
                responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.WORK_WITH_ACCOUNT_HELP);
                receiveCommand();
            }
        }
    }

    /**
     * Receive command and transfer request to the required methods, also calls receiveCommand() method
     * and give response
     * @see UserController#receiveCommand()
     */
    private void adminPanel() {
        response(new AdminPanelResponse());
        String request = userRequest.getRequest();
        if (isCommand(request)){
            switch (request){
                case "/users" -> viewAllUsers();
                case "/auditor" -> auditor();
                case "/logout" -> logout();
                default -> receiveCommand();
            }
        } else {
            response(new AdminPanelResponse());
            receiveCommand();
        }
    }
    /**
     * Endpoint for /auditor
     * @see UserService#processAdminPanelViewUserActionsAuditor()
     */
    private void auditor() {
        userService.processAdminPanelViewUserActionsAuditor();
        receiveCommand();
    }
    /**
     * Endpoint for /users
     * @see UserService#processAdminPanelViewAllUsers()
     */
    private void viewAllUsers() {
        userService.processAdminPanelViewAllUsers();
        receiveCommand();
    }

    /**
     * Exit from application
     */
    private void exit() {
        System.out.println("======= Exit from Wallet Service Application =======");
    }

    /**
     * Calls UserService and endpoint for /login
     * @see UserService#processLogin(String)
     */
    public void login(){
        responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.LOGIN_HELP);
        if (userService.processLogin(userRequest.getRequest())){
            response(new SuccessMessageResponse());
            receiveCommand();
        }
        else {
            responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.NOT_REGISTERED);
            userService.processRegistration(this.userRequest.getRequest());
        }
    }
    /**
     * Calls UserService and endpoint for /register
     * @see UserService#processRegistration(String)
     */
    public void registration(){
        responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.NOT_REGISTERED);
        User appUser = userService.processRegistration(this.userRequest.getRequest());
        if (appUser != null){
            responseWithMessage(new SuccessMessageResponse(),appUser.toString());
            receiveCommand();
        }
        else {
            response(new ErrorMessageResponse());
            receiveCommand();
        }
    }
    /**
     * Endpoint for /logout
     * @see UserService#processLogout()
     */
    private void logout() {
        userService.processLogout();
        receiveCommand();
    }
    /**
     * Endpoint for /history
     * @see UserService#getUserTransactionalHistory()
     */
    private void history() {
        userService.getUserTransactionalHistory();
        receiveCommand();
    }

    /**
     * Endpoint for /wallet
     * @see UserService#getUserWallet()
     */
    private void wallet() {
        userService.getUserWallet();
        receiveCommand();
    }
    /**
     * Receive command and calls TransactionService methods, also calls receiveCommand() method
     * and give response
     * @see UserController#receiveCommand()
     */
    private void transaction() {
        responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.TRANSACTIONAL_HELP);
        String request = this.userRequest.getRequest();
        if (isCommand(request)){
            switch (request){
                case "/debit" -> {
                    responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.WORK_WITH_TRANSACTION_HELP);
                    if (transactionalService.processDebitTransaction(userRequest.getRequest(),
                            userService.getCurrentAppUser())){
                        response(new SuccessMessageResponse());
                    } else {
                        response(new FailedMessageResponse());
                    }
                    userService.updateUser(userService.getCurrentAppUser());
                    receiveCommand();
                }
                case "/credit" -> {
                    responseWithMessage(new HelpMessageResponse(),HelpMessageResponse.WORK_WITH_TRANSACTION_HELP);
                    if (transactionalService.processCreditTransaction(userRequest.getRequest(),
                            userService.getCurrentAppUser())){
                        response(new SuccessMessageResponse());
                    } else {
                        response(new FailedMessageResponse());
                    }
                    receiveCommand();
                    userService.updateUser(userService.getCurrentAppUser());
                }
                default -> receiveCommand();
                }
            }
    }

    /**
     * Checking command from request
     * @param command String
     * @return boolean result of method
     */
    private static boolean isCommand(String command){
        return command.startsWith("/");
    }

    /**
     * Give a response
     * @param response ApplicationResponse
     * @see ApplicationResponse#executeResponse()
     */
    private void response(ApplicationResponse response){
        response.executeResponse();
    }

    /**
     * Give a response with message
     * @param response ApplicationResponse
     * @param message String message
     * @see ApplicationResponse#executeResponse(String)
     */
    private void responseWithMessage(ApplicationResponse response, String message){
        response.executeResponse(message);
    }
}
