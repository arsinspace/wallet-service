package ru.ylab.repository;

import lombok.Cleanup;
import ru.ylab.utils.db.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * Basic interface that provides operations for operating a wallet entity in Database
 */
public interface WalletRepository {
    /**
     * This field contains sql query for update wallet entity in DB
     */
    String UPDATE_QUERY = "update wallet.wallet set balance = ? where user_id = ?";

    /**
     * This method open connection to DB and update wallet entity in Database
     * @param userId Long userId
     * @param amount Integer new user balance
     * @return Integer balance value
     */
    static int updateBalance(long userId, int amount){
            try {
                Connection connection = ConnectionPool.getInstanceConnection().getConnection();
                @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
                preparedStatement.setInt(1, amount);
                preparedStatement.setLong(2, userId);
                preparedStatement.executeUpdate();
                connection.commit();
                ConnectionPool.getInstanceConnection().closeConnection(connection);
            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException exception) {
                System.out.println("Error to save user - " + exception);
                return 0;
            }
            return 0;
        }
    }

