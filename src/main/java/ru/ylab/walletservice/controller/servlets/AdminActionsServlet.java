package ru.ylab.walletservice.controller.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dao.UserActionDAO;
import ru.ylab.walletservice.dao.UserDAO;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * This servlet contains logic for endpoints /user-actions, /users and consumes GET method
 */
@WebServlet(name = "AdminActionsServlet", value = {"/user-actions", "/users"})
public class AdminActionsServlet extends HttpServlet {
    /**
     * Filed contains link to UserActionDAO
     */
    private UserActionDAO userActionDAO;
    /**
     * Filed contains link to UserDAO
     */
    private UserDAO userDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userDAO = new UserDAO();
        userActionDAO = new UserActionDAO();
        super.init(config);
    }
    @Loggable
    @TrackEvent(action = "Admin GET userActions")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().contains("/user-actions")){
            sendResponseAsList(resp, userActionDAO.findAllUserActions());

        } else if (req.getRequestURI().contains("/users")){
            sendResponseAsList(resp, userDAO.findAllUsers());
        }
        else {
            SendResponse.getInstance().sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "bad request");
        }
    }

    /**
     * This method send response with JSONs
     * @param response HttpServletResponse
     * @param list List<?>
     * @throws IOException
     */
    @TrackEvent(action = "Admin GET list of Users")
    private static void sendResponseAsList(HttpServletResponse response, List<?> list) throws IOException {
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(list);
    }
}
