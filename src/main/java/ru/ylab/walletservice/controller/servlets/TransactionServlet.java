package ru.ylab.walletservice.controller.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dao.TransactionDAO;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.services.TransactionService;
import ru.ylab.walletservice.services.impl.TransactionServiceImpl;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
/**
 * This servlet contains logic for endpoints /transaction/debit, /transaction/credit
 * /history and consumes POST(create credit or debit transaction) and GET(get all users transactions) methods
 */
@WebServlet(name = "TransactionServlet", urlPatterns = {"/transaction/debit", "/transaction/credit", "/history"})
public class TransactionServlet extends HttpServlet {
    /**
     * This field contains link to TransactionService
     */
    private TransactionService service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = new TransactionServiceImpl(new TransactionDAO());
        super.init(config);
    }
    @Loggable
    @TrackEvent(action = "User get his transactions history")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().contains("/history")){
            List<Transaction> transactions = service.getAllTransactions(getUserId(req));
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.println(transactions);
        }
        else {
            SendResponse.getInstance().sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "bad request");
        }
    }
    @Loggable
    @TrackEvent(action = "User working with transactions")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().contains("/debit")) {
            service.processDebitTransaction(req,resp,getUserId(req));
        }
        else if (req.getRequestURI().contains("/credit")){
            service.processCreditTransaction(req,resp,getUserId(req));
        } else {
            SendResponse.getInstance().sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "bad request");
        }
    }

    /**
     * This method search userId in cookies
     * @param request HttpServletRequest
     * @return Long userId
     */
    private long getUserId(HttpServletRequest request) {

        if (request.getCookies() != null &&
            Arrays.stream(request.getCookies())
                    .anyMatch(el -> el.getName().equals("userId")
                    )) {
            return Arrays.stream(request.getCookies())
                    .filter(el -> el.getName().equals("userId"))
                    .map(Cookie::getValue)
                    .map(Long::valueOf)
                    .findAny().get();
        }
        return 0;
    }
}



