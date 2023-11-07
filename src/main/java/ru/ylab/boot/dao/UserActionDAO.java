package ru.ylab.boot.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ylab.boot.model.UserAction;
import ru.ylab.boot.repository.UserActionRepository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class provides operations for operating a User actions entity in Database
 */
@Repository
@RequiredArgsConstructor
public class UserActionDAO implements UserActionRepository {

    private final JdbcTemplate jdbcTemplate;

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

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE_QUERY);
            ps.setLong(1, user_id);
            ps.setString(2, action);
            ps.setString(3,status);
            ps.setTimestamp(4,time);
            return ps;
        });
    }

    /**
     * This method find all user actions entities in Database
     * @return List of all UserAction
     */
    public List<UserAction> findAllUserActions(){

        return jdbcTemplate.query(FIND_ALL_BY_USER_ID_QUERY, rs -> {
            List<UserAction> userActions = new ArrayList<>();
            while (rs.next()){
                userActions.add(UserAction.builder()
                        .id(rs.getLong("user_action_id"))
                        .userId(rs.getLong("user_id"))
                        .action(rs.getString("action"))
                        .status(rs.getString("status"))
                        .time(rs.getTimestamp("time"))
                        .build());
            }
            return userActions;
        });
    }
}
