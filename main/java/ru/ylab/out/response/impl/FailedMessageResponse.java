package ru.ylab.out.response.impl;

import ru.ylab.out.response.ApplicationResponse;
/**
 * @implNote This implementation contains failed messages of the application's responses to user requests
 */
public class FailedMessageResponse implements ApplicationResponse {
    /**
     * @see ApplicationResponse#executeResponse(String)
     */
    @Override
    public void executeResponse(String message) {
        System.out.println(message);
    }
    /**
     *@see ApplicationResponse#executeResponse()
     */
    @Override
    public void executeResponse() {
        System.out.println("Failed");
    }
}
