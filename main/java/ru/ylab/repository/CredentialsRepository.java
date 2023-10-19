package ru.ylab.repository;

import lombok.Cleanup;
import ru.ylab.utils.db.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Basic interface that provides operations for operating a user credentials entity in Database
 */
public interface CredentialsRepository {
    /**
     * This field contains sql query for find user credentials entity in DB
     */
    String FIND_CREDENTIAL_BY_USERNAME = "select * from wallet.credentials where username = ?";

    /**
     * This method checking is login used in Database
     * @param userName String users username
     * @return boolean result of checking
     */
    static boolean isLoginUsed(String userName){

        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(FIND_CREDENTIAL_BY_USERNAME);
            preparedStatement.setString(1,userName);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
            return resultSet.next();
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException exception) {
            System.out.println("Error to find credentials - " + exception);
            return false;
        }
    }
}
