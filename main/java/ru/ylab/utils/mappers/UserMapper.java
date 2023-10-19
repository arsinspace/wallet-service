package ru.ylab.utils.mappers;

import ru.ylab.model.User;
import ru.ylab.model.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class for convert to User object from ResultSet
 */
public class UserMapper {
    /**
     * This method convert to User object from ResultSet
     * @param resultSet ResultSet
     * @return User entity
     */
    public static User convertToUser(ResultSet resultSet){
        User user = null;
        try {
            user = User.builder()
                    .id(resultSet.getLong("user_id"))
                    .name(resultSet.getString("name"))
                    .lastName(resultSet.getString("last_name"))
                    .age(resultSet.getString("age"))
                    .wallet(new Wallet(resultSet.getInt("balance")))
                    .build();
        } catch (SQLException exception){
            System.out.println("Error to convert User");
        }
        return user;
    }
}
