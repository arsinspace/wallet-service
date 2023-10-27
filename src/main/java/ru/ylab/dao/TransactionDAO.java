package ru.ylab.dao;

import lombok.Cleanup;
import ru.ylab.model.Transactional;
import ru.ylab.repository.TransactionalRepository;
import ru.ylab.utils.db.ConnectionPool;
import ru.ylab.utils.mappers.TransactionMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  This implementation contains the logic for working with transactions in DB
 */
public class TransactionDAO implements TransactionalRepository {
    /**
     * This field contains sql query for matching transactions in DB
     */
    private static final String FIND_ANY_MATCH_TRANSACTION_BY_ID_QUERY = """
            select * from wallet.transactions
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
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_ANY_MATCH_TRANSACTION_BY_ID_QUERY);
            preparedStatement.setString(1,id);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
            return resultSet.next();
        } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
            System.out.println("Error to find transaction ID - " + exception);
            return false;
        }
    }

    @Override
    public List<Transactional> findAllByUserId(long id) {
        List<Transactional> transactions = new ArrayList<>();
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_ALL_BY_USER_ID_QUERY);
            preparedStatement.setLong(1,id);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
            if (resultSet.next()){
                while (resultSet.next()){
                    transactions.add(TransactionMapper.convertToTransaction(resultSet));
                }
            }
            return transactions;
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException exception) {
            System.out.println("Error to find transaction ID - " + exception);
            return transactions;
        }
    }

    @Override
    public long saveTransaction(Transactional transaction){

        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY,
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, transaction.getUserId());
            preparedStatement.setString(2, transaction.getTransactionalId());
            preparedStatement.setString(3, transaction.getPurpose());
            preparedStatement.setInt(4, transaction.getAmount());
            preparedStatement.setString(5, transaction.getStatus().toString());
            preparedStatement.setTimestamp(6, transaction.getTransactionalTime());
            preparedStatement.executeUpdate();
            connection.commit();

            @Cleanup ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getLong(1));
                return transaction.getId();
            }
            } catch(IOException | ClassNotFoundException | InterruptedException | SQLException exception){
            System.out.println("Error to save transaction - " + exception);
            return transaction.getId();
        }
        return transaction.getId();
    }
}
