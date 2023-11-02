package ru.ylab.walletservice.dao;

import lombok.Cleanup;
import ru.ylab.walletservice.model.Credentials;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.model.Wallet;
import ru.ylab.walletservice.repository.UserRepository;
import ru.ylab.walletservice.utils.db.ConnectionPool;
import ru.ylab.walletservice.utils.mappers.UserMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *  This implementation contains the logic for working with users entities in DB
 */
public class UserDAO implements UserRepository {
    /**
     * This field contains sql query for find all users in DB
     */
    private static final String FIND_ALL_QUERY = "select * from wallet.users";
    /**
     * This field contains sql query for find save user in DB
     */
    private static final String SAVE_QUERY = "insert into wallet.users (name, last_name, age) values (?,?,?)";
    /**
     * This field contains sql query for find save user credentials in DB
     */
    private static final String SAVE_CREDENTIALS_QUERY = "insert into wallet.credentials (user_id, username, password) " +
            "values (?,?,?)";
    /**
     * This field contains sql query for find save users wallet in DB
     */
    private static final String SAVE_WALLET_QUERY = "insert into wallet.wallet (user_id, balance) values(?,?)";
    /**
     * This field contains sql query for find user in DB by his credentials
     */
    private static final String FIND_USER_BY_CREDENTIALS = """
            select u.user_id, u.name, last_name, age, wa.balance
            from wallet.users u
            inner join wallet.credentials cr on u.user_id = cr.user_id
            inner join wallet.wallet wa on u.user_id = wa.user_id
            where cr.username = ? and cr.password = ?""";

    @Override
    public long saveUser(User user){

               try {
                   Connection connection = ConnectionPool.getInstanceConnection().getConnection();
                   @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY,
                           Statement.RETURN_GENERATED_KEYS);
                   preparedStatement.setString(1, user.getName());
                   preparedStatement.setString(2, user.getLastName());
                   preparedStatement.setString(3, user.getAge());
                   preparedStatement.executeUpdate();
                   connection.commit();
                   @Cleanup ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                   ConnectionPool.getInstanceConnection().closeConnection(connection);
                   if (generatedKeys.next()){
                       user.setId(generatedKeys.getLong(1));
                       saveUserCredentials(user.getId(), user.getCredentials());
                       saveUserWallet(user.getId(), new Wallet(0));
                       return user.getId();
                   }
               } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
                   System.out.println("Error to save user - " + exception);
                   return user.getId();
               }
        return user.getId();
    }
    private void saveUserCredentials(Long userId,Credentials credentials){
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CREDENTIALS_QUERY);
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, credentials.getLogin());
            preparedStatement.setString(3, credentials.getPassword());
            preparedStatement.executeUpdate();
            connection.commit();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
            System.out.println("Error to save user credentials - " + exception);
        }
    }
    private void saveUserWallet(Long userId,Wallet wallet){
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(SAVE_WALLET_QUERY);
            preparedStatement.setLong(1, userId);
            preparedStatement.setInt(2, wallet.getBalance());
            preparedStatement.executeUpdate();
            connection.commit();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
            System.out.println("Error to save user wallet - " + exception);
        }
    }

    @Override
    public Optional<User> findUserByCredentials(Credentials credentials){
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_CREDENTIALS);
            preparedStatement.setString(1, credentials.getLogin());
            preparedStatement.setString(2, credentials.getPassword());
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
            if (resultSet.next()){
                return Optional.of(UserMapper.convertToUser(resultSet));
            }
            return Optional.empty();
        } catch (InterruptedException | SQLException | ClassNotFoundException | IOException exception) {
            System.out.println("Error to find user by Credentials - " + exception);
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = ConnectionPool.getInstanceConnection().getConnection();
            @Cleanup PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_ALL_QUERY);
            @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
            ConnectionPool.getInstanceConnection().closeConnection(connection);
            if (resultSet.next()){
                while (resultSet.next()){
                    users.add(User.builder()
                            .id(resultSet.getLong("user_id"))
                            .name(resultSet.getString("name"))
                            .lastName(resultSet.getString("last_name"))
                            .age(resultSet.getString("age"))
                            .build());
                }
            }
            return users;
        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException exception) {
            System.out.println("Error to find transaction ID - " + exception);
            return users;
        }
    }
}
