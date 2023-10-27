package ru.ylab.walletservice.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.dao.WalletDAO;
import ru.ylab.walletservice.dto.TransactionDTO;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.model.enums.TransactionStatus;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.repository.TransactionalRepository;
import ru.ylab.walletservice.services.TransactionService;
import ru.ylab.walletservice.utils.JsonConverter;
import ru.ylab.walletservice.utils.mappers.TransactionDTOMapper;
import ru.ylab.walletservice.utils.validatos.TransactionDTOValidator;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * This implementation contains the logic for working with transactions
 */
public class TransactionServiceImpl implements TransactionService {
    /**
     * Field contains a link to the object TransactionMemory
     */
    private final TransactionalRepository transactionDAO;

    public TransactionServiceImpl(TransactionalRepository transactionDAO) {
        this.transactionDAO = transactionDAO;
    }


    @Override
    public boolean processDebitTransaction(HttpServletRequest request, HttpServletResponse response, long userId)
            throws IOException {

        Transaction transaction = null;
        int balance = WalletDAO.findWalletByUserId(userId);

        try {
            TransactionDTO transactionDTO = JsonConverter.getMapper()
                    .readValue(request.getInputStream(), TransactionDTO.class);
            //validation DTO from request
            TransactionDTOValidator.getInstance().isValid(transactionDTO);
            //mapping from dto to Transaction
            transaction = TransactionDTOMapper.INSTANCE.toTransaction(transactionDTO);
        } catch (Exception e){
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }

        if (transaction == null) {
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_BAD_REQUEST,"No valid JSON, try again");
            return false;
        }
        if (!checkTransaction(transaction)){
            int currentBalance = balance - transaction.getAmount();
            if (currentBalance >= 0){
                WalletDAO.updateBalance(userId,currentBalance);
                transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
                transaction.setUserId(userId);
                transaction.setStatus(TransactionStatus.SUCCESSFUL);
                transactionDAO.saveTransaction(transaction);
                SendResponse.getInstance().sendResponse(response,
                        HttpServletResponse.SC_CREATED,"Transaction created");
                return true;
            }
            else {
                transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
                transaction.setUserId(userId);
                transaction.setStatus(TransactionStatus.FAILED);
                transactionDAO.saveTransaction(transaction);
                SendResponse.getInstance().sendResponse(response,
                        HttpServletResponse.SC_EXPECTATION_FAILED, "Not enough funds in the account");
                return false;
            }
        } else{
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(userId);
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.saveTransaction(transaction);
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_EXPECTATION_FAILED, "No valid transaction id");
            return false;
        }
    }

    @Override
    public boolean processCreditTransaction(HttpServletRequest request, HttpServletResponse response, long userId)
            throws IOException {
        Transaction transaction = null;
        int balance = WalletDAO.findWalletByUserId(userId);

        try {
            TransactionDTO transactionDTO = JsonConverter.getMapper()
                    .readValue(request.getInputStream(), TransactionDTO.class);
            //validation DTO from request
            TransactionDTOValidator.getInstance().isValid(transactionDTO);
            //mapping from dto to Transaction
            transaction = TransactionDTOMapper.INSTANCE.toTransaction(transactionDTO);
        } catch (Exception e){
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }

        if (transaction == null) {
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_BAD_REQUEST,"No valid JSON, try again");
            return false;
        }
        if (!checkTransaction(transaction)) {
            int currentBalance = balance + transaction.getAmount();
            WalletDAO.updateBalance(userId,currentBalance);
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(userId);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transactionDAO.saveTransaction(transaction);
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_CREATED,"Transaction created");
            return true;
        } else {
            transaction.setTransactionalTime(new Timestamp(System.currentTimeMillis()));
            transaction.setUserId(userId);
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDAO.saveTransaction(transaction);
            SendResponse.getInstance().sendResponse(response,
                    HttpServletResponse.SC_EXPECTATION_FAILED, "No valid transaction id");
            return false;
        }
    }

    @Override
    public List<Transaction> getAllTransactions(long userId) {
            return transactionDAO.findAllByUserId(userId);
    }
    /**
     * Transaction validity check
     * @param transaction transaction entity
     * @return boolean result of checking
     */
    private boolean checkTransaction(Transaction transaction){
            return transactionDAO.anyMatchTransactionalById(transaction.getTransactionalId());
    }
}
