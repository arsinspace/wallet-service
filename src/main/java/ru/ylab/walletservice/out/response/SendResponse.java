package ru.ylab.walletservice.out.response;

import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.utils.mappers.JsonConverter;

import java.io.IOException;
import java.util.Collections;

/**
 * Singleton class for work with responses
 */
public class SendResponse {

    private static final SendResponse SEND_RESPONSE = new SendResponse();

    public static SendResponse getInstance(){
        return SEND_RESPONSE;
    }

    /**
     * Method send response to user
     * @param response HttpServletResponse
     * @param statusCode Integer
     * @param message String
     * @throws IOException
     */
    public void sendResponse(HttpServletResponse response, int statusCode, String message)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getOutputStream().write(JsonConverter.getMapper()
                .writeValueAsBytes(Collections.singletonMap("message", message)));
    }
}
