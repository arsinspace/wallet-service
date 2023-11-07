package ru.ylab.walletservice.utils.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ylab.walletservice.model.Credentials;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.model.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Class for convert to User object from ResultSet
 */
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = null;
        try {
            user = User.builder()
                    .id(rs.getLong("user_id"))
                    .name(rs.getString("name"))
                    .lastName(rs.getString("last_name"))
                    .age(rs.getString("age"))
                    .wallet(new Wallet(rs.getInt("balance")))
                    .credentials(new Credentials(rs.getString("username"),
                            rs.getString("password")))
                    .roles(List.of("ROLE_USER"))
                    .build();
            if (user.getCredentials().getLogin().equals("admin")){
                user.setRoles(List.of("ROLE_ADMIN"));
            }
        } catch (SQLException exception){
            System.out.println("Error to convert User");
        }
        return user;
    }
}
