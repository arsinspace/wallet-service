package ru.ylab.walletservice.controller.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dao.WalletDAO;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.io.IOException;
import java.util.Arrays;

/**
 * This servlet contains logic for endpoint "/wallet" and consumes GET method
 */
@WebServlet(name = "WalletServlet", value = "/wallet")
public class WalletServlet extends HttpServlet {
    @Loggable
    @TrackEvent(action = "User look his wallet funds")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getCookies() != null &&
                Arrays.stream(req.getCookies())
                        .anyMatch(el -> el.getName().equals("userId")
                        )) {
            long userId = Arrays.stream(req.getCookies())
                    .filter(el -> el.getName().equals("userId"))
                    .map(Cookie::getValue)
                    .map(Long::valueOf)
                    .findAny().get();

            SendResponse.getInstance().sendResponse(resp, HttpServletResponse.SC_FOUND,
                    String.valueOf(WalletDAO.findWalletByUserId(userId)));
        }
    }
}
