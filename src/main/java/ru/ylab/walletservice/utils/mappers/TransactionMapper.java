package ru.ylab.walletservice.utils.mappers;

import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.model.enums.TransactionStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class for convert to transaction object from ResultSet
 */
public class TransactionMapper {
    /**
     * This method convert to transaction object from ResultSet
     * @param resultSet ResultSet
     * @return Transaction entity
     */
    public static Transaction convertToTransaction(ResultSet resultSet){
        Transaction transaction = null;
        try {
            transaction = Transaction.builder()
                    .id(resultSet.getLong("transaction_id"))
                    .userId(resultSet.getLong("user_id"))
                    .transactionalId(resultSet.getString("users_transaction_name"))
                    .purpose(resultSet.getString("purpose"))
                    .amount(resultSet.getInt("amount"))
                    .status(TransactionStatus.valueOf(resultSet.getString("transaction_status")))
                    .transactionalTime(resultSet.getTimestamp("transaction_time"))
                    .build();
        } catch (SQLException exception){
            System.out.println("Error to convert User");
        }
        return transaction;
    }
}
