package ru.ylab.out.response.impl;

import ru.ylab.out.response.ApplicationResponse;

/**
 * @implNote This implementation contains the application's responses to administrator requests
 */
public class AdminPanelResponse implements ApplicationResponse {
    /**
     * @see ApplicationResponse#executeResponse(String)
     */
    @Override
    public void executeResponse(String message) {

    }

    /**
     *@see ApplicationResponse#executeResponse()
     */
    @Override
    public void executeResponse() {
        System.out.println("""
                ======= Welcome to Admin panel =======
                /users - View all users
                /auditor - View all users actions
                /logout - Logout from admin panel
                """);
    }
}
