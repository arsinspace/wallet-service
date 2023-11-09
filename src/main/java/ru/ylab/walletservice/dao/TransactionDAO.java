package ru.ylab.walletservice.dao;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ylab.walletservice.model.Transaction;
import ru.ylab.walletservice.model.enums.TransactionStatus;
import ru.ylab.walletservice.repository.TransactionalRepository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  This implementation contains the logic for working with transactions in DB
 */
@Repository
@RequiredArgsConstructor
public class TransactionDAO implements TransactionalRepository {

    private final JdbcTemplate jdbcTemplate;
    /**
     * This field contains sql query for matching transactions in DB
     */
    private static final String FIND_ANY_MATCH_TRANSACTION_BY_ID_QUERY = """
            select COUNT(*) from wallet.transactions
            where users_transaction_name = ?
            """;
    /**
     * This field contains sql query for saving transactions in DB
     */
    private static final String SAVE_QUERY = "insert into wallet.transactions (user_id, users_transaction_name, purpose, " +
            "amount, transaction_status, transaction_time) values (?,?,?,?,?,?)";

    /**
     * This field contains sql query for find all transactions in DB
     */
    private static final String FIND_ALL_BY_USER_ID_QUERY = "select * from wallet.transactions where user_id = ?";
    @Override
    public boolean anyMatchTransactionalById(String id){
        Integer count = jdbcTemplate.queryForObject(FIND_ANY_MATCH_TRANSACTION_BY_ID_QUERY,
                new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public List<Transaction> findAllByUserId(long id) {

        return jdbcTemplate.query(FIND_ALL_BY_USER_ID_QUERY, rs -> {
            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()){
                transactions.add(Transaction.builder()
                        .id(rs.getLong("transaction_id"))
                        .userId(rs.getLong("user_id"))
                        .transactionalId(rs.getString("users_transaction_name"))
                        .purpose(rs.getString("purpose"))
                        .amount(rs.getInt("amount"))
                        .status(TransactionStatus.valueOf(rs.getString("transaction_status")))
                        .transactionalTime(rs.getTimestamp("transaction_time"))
                        .build());
            }
            return transactions;
        }, id);
    }

    @Override
    public long saveTransaction(Transaction transaction){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, transaction.getUserId());
            ps.setString(2, transaction.getTransactionalId());
            ps.setString(3, transaction.getPurpose());
            ps.setInt(4, transaction.getAmount());
            ps.setString(5, transaction.getStatus().toString());
            ps.setTimestamp(6, transaction.getTransactionalTime());
            return ps;
        }, keyHolder);
        return (long) keyHolder.getKeys().get("transaction_id");
    }
}
