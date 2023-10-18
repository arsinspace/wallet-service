package ru.ylab.out.response.impl;

import ru.ylab.out.response.ApplicationResponse;
/**
 * @implNote This implementation contains errors of the application's responses to user requests
 */
public class ErrorMessageResponse implements ApplicationResponse {
    /**
     * @see ApplicationResponse#executeResponse(String)
     */
    @Override
    public void executeResponse(String message) {
        System.out.println("Error: " + message);
    }
    /**
     *@see ApplicationResponse#executeResponse()
     */
    @Override
    public void executeResponse() {
        System.out.println("Error");
    }
}
