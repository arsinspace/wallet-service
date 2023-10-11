package ru.ylab.out.response;

/**
 * Base interface
 * that represents the application's response to the user
 */
public interface ApplicationResponse {
    /**
     * Method prints a message to the console
     * @param message custom message
     */
    void executeResponse(String message);

    /**
     * Overridden method, outputs a message to the console
     */
    void executeResponse();
}
