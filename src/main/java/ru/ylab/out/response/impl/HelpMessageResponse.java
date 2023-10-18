package ru.ylab.out.response.impl;

import ru.ylab.out.response.ApplicationResponse;
/**
 * @implNote This implementation contains help messages of the application's responses to user requests
 */
public class HelpMessageResponse implements ApplicationResponse {
    /**
     * Registration on system message
     */
    public static final String NOT_REGISTERED = """
            Registration on system:
            TYPE JSON FORMAT TO REGISTER
            Example:
            {"name":"Adam","lastName":"Adam","age":"29","credentials":{"login":"adam","password":"123"}}
            """;
    /**
     * Commands with transactional message
     */
    public static final String TRANSACTIONAL_HELP = """
            Commands with transactional:
            /debit - Debits funds from your wallet
            /credit - Credits funds to your wallet
            """;
    /**
     * How to work with transaction message
     */
    public static final String WORK_WITH_TRANSACTION_HELP = """
            Registration transaction:
            TYPE JSON FORMAT TO REGISTER TRANSACTION
            Transaction ID must be unique
            Example:
            {"transactionalId":"yourId","purpose":"example","amount":123}
            """;
    /**
     * How to work with user account message
     */
    public static final String WORK_WITH_ACCOUNT_HELP = """
            ======= Welcome to your account =======
            Registration transaction:
            /transaction
            See your wallet funds:
            /wallet
            See your transactional history:
            /history
            Logout:
            /logout
            """;
    /**
     * How to login message
     */
    public static final String LOGIN_HELP = """
            Login to system:
            TYPE JSON FORMAT TO LOGIN
            Example:
            {"login":"adam","password":"123"}
            """;
    /**
     * @see ApplicationResponse#executeResponse(String)
     */
    @Override
    public void executeResponse(String message) {
        System.out.println("Your actions in this task: " + "\n" + message);
    }
    /**
     *@see ApplicationResponse#executeResponse()
     */
    @Override
    public void executeResponse() {
        System.out.println("""
                How to start work with system:
                Commands:
                /login - Login to system
                /register - Register new User
                /exit - Exit from application
                """);
    }
}
