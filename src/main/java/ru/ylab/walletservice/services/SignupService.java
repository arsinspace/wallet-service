package ru.ylab.walletservice.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
/**
 * Basic interface providing operations for working with signup
 */
public interface SignupService {
    /**
     * Contains logic for working with signup request
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException
     */
    void processSignup(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
