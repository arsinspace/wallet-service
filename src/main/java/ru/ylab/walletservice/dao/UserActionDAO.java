package ru.ylab.walletservice.dao;

import lombok.Cleanup;
import org.springframework.stereotype.Repository;
import ru.ylab.walletservice.model.UserAction;
import ru.ylab.walletservice.utils.db.ConnectionPool;
import ru.ylab.walletservice.utils.mappers.UserActionMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Class provides operations for operating a User actions entity in Database
 */
@Repository
public class UserActionDAO {

    /**
     * This field contains sql query for save user action entity in DB
     */
   private static final String SAVE_QUERY = "insert into wallet.user_action (user_id, action, status, time) " +
            "values (?,?,?,?)";
    /**
     * This field contains sql query for find all users action entity in DB
     */
   private static final String FIND_ALL_BY_USER_ID_QUERY = "select * from wallet.user_action";

    /**
     * This method saving entity in Database
     * @param user_id Long userId
     * @param action String user action
     */
    public void saveUserAction(long user_id, String action, String status, Timestamp time){
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();

            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY);
            preparedStatement.setLong(1, user_id);
            preparedStatement.setString(2, action);
            preparedStatement.setString(3,status);
            preparedStatement.setTimestamp(4,time);
            preparedStatement.executeUpdate();
            connection.commit();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
            System.out.println("Error to save userAction - " + exception);
        }
    }

    /**
     * This method find all user actions entities in Database
     * @return List of all UserAction
     */
    public List<UserAction> findAllUserActions(){

        List<UserAction> userActions = new ArrayList<>();
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_ALL_BY_USER_ID_QUERY);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

            ConnectionPool.getInstanceConnection().closeConnection(connection);
            if (resultSet.next()){
                while (resultSet.next()){
                    userActions.add(UserActionMapper.convertToUserAction(resultSet));
                }
            }
            return userActions;
        } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
            System.out.println("Error to find user actions - " + exception);
            return userActions;
        }
    }
}
