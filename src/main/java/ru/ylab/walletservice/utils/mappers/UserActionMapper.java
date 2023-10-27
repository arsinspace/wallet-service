package ru.ylab.walletservice.utils.mappers;

import ru.ylab.walletservice.model.UserAction;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Class for convert to UserAction object from ResultSet
 */
public class UserActionMapper {
    /**
     * This method convert to UserAction object from ResultSet
     * @param resultSet ResultSet
     * @return UserAction entity
     */
    public static UserAction convertToUserAction(ResultSet resultSet) {

        UserAction userAction = null;
        try {
            userAction = UserAction.builder()
                    .id(resultSet.getLong("user_action_id"))
                    .userId(resultSet.getLong("user_id"))
                    .action(resultSet.getString("action"))
                    .status(resultSet.getString("status"))
                    .time(resultSet.getTimestamp("time"))
                    .build();
        } catch (SQLException exception) {
            System.out.println("Error to convert UserAction");
        }
        return userAction;
    }
}
