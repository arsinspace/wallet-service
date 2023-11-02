package ru.ylab.walletservice.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Basic interface providing operations for working with credentials
 */
public interface LoginService {
    /**
     * Contains logic for working with login request
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException
     * @throws ServletException
     */
    void processLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;
}
