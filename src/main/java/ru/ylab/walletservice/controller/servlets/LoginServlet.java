package ru.ylab.walletservice.controller.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dao.UserDAO;
import ru.ylab.walletservice.services.LoginService;
import ru.ylab.walletservice.services.impl.LoginServiceImpl;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.io.IOException;
/**
 * This servlet contains logic for endpoint /login and consumes POST method
 */
@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    /**
     * This field contains link to LoginService
     */
    private LoginService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = new LoginServiceImpl(new UserDAO());
        super.init(config);
    }
    @Loggable
    @TrackEvent(action = "User login in application")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.processLogin(req, resp);
    }
}
