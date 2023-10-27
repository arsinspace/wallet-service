package ru.ylab.walletservice.controller.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dao.UserDAO;
import ru.ylab.walletservice.services.SignupService;
import ru.ylab.walletservice.services.impl.SignupServiceImpl;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.io.IOException;
/**
 * This servlet contains logic for endpoint /signup and consumes POST method
 */
@WebServlet(name = "SignupServlet", value = "/signup")
public class SignupServlet extends HttpServlet {

    /**
     * This field contains link to SignupService
     */
    private SignupService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = new SignupServiceImpl(new UserDAO());
        super.init(config);
    }
    @Loggable
    @TrackEvent(action = "User signup to application")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       service.processSignup(req, resp);
    }
}
