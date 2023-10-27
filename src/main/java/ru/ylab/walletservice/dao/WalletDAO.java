package ru.ylab.walletservice.dao;

import lombok.Cleanup;
import ru.ylab.walletservice.utils.db.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class provides operations for operating a wallet entity in Database
 */
public class WalletDAO {

    private static final WalletDAO WALLET_DAO = new WalletDAO();

    /**
     * This field contains sql query for update wallet entity in DB
     */
    private static final String UPDATE_QUERY = "update wallet.wallet set balance = ? where user_id = ?";
    private static final String FIND_BY_USER_ID = "select balance from wallet.wallet where user_id = ?";

    public static WalletDAO getInstance(){
        return WALLET_DAO;
    }

    /**
     * This method open connection to DB and update wallet entity in Database
     * @param userId Long userId
     * @param amount Integer new user balance
     * @return Integer balance value
     */
     public static int updateBalance(long userId, int amount){
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setInt(1, amount);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
            connection.commit();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException exception) {
            System.out.println("Error to update wallet - " + exception);
            return 0;
        }
        return 0;
    }

    public static int findWalletByUserId(long userId){
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_USER_ID);
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
            connection.commit();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException exception) {
            System.out.println("Error to find wallet - " + exception);
            return 0;
        }
        return 0;
    }
}
