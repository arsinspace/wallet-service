package ru.ylab.walletservice.utils.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.model.enums.TransactionStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class for convert to transaction object from ResultSet
 */
public class TransactionMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = null;
        try {
            transaction = Transaction.builder()
                    .id(rs.getLong("transaction_id"))
                    .userId(rs.getLong("user_id"))
                    .transactionalId(rs.getString("users_transaction_name"))
                    .purpose(rs.getString("purpose"))
                    .amount(rs.getInt("amount"))
                    .status(TransactionStatus.valueOf(rs.getString("transaction_status")))
                    .transactionalTime(rs.getTimestamp("transaction_time"))
                    .build();
        } catch (SQLException exception){
            System.out.println("Error to convert Transaction");
        }
        return transaction;
    }
}
