package ru.ylab.walletservice.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.ylab.walletservice.model.Credentials;
import ru.ylab.walletservice.model.User;
import ru.ylab.walletservice.model.Wallet;
import ru.ylab.walletservice.repository.UserRepository;
import ru.ylab.walletservice.utils.mappers.UserMapper;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

/**
 *  This implementation contains the logic for working with users entities in DB
 */
@Repository
@RequiredArgsConstructor
public class UserDAO implements UserRepository {

   private final JdbcTemplate jdbcTemplate;
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
    /**
     * This field contains sql query for find user in DB by his username
     */
    private static final String FIND_USER_BY_USERNAME = """
            select u.user_id, u.name, last_name, age, wa.balance, cr.username, cr.password
            from wallet.users u
            inner join wallet.credentials cr on u.user_id = cr.user_id
            inner join wallet.wallet wa on u.user_id = wa.user_id
            where cr.username = ?""";
    /**
     * This field contains sql query for find userId in DB by his username
     */
    private static final String FIND_USER_ID_BY_USERNAME = """
            select u.user_id
            from wallet.users u
            inner join wallet.credentials cr on u.user_id = cr.user_id
            where username = ?
            """;
    @Override
    public long saveUser(User user){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getAge());
            return ps;
        }, keyHolder);
        System.out.println(Objects.requireNonNull(keyHolder.getKeys()).get("user_id").toString());
        saveUserCredentials((Long) keyHolder.getKeys().get("user_id"),user.getCredentials());
        saveUserWallet((Long) keyHolder.getKeys().get("user_id"),new Wallet(0));

        return (long) keyHolder.getKeys().get("user_id");

    }
    private void saveUserCredentials(Long userId,Credentials credentials){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE_CREDENTIALS_QUERY);
            ps.setLong(1, userId);
            ps.setString(2, credentials.getLogin());
            ps.setString(3, credentials.getPassword());
            return ps;
        });
    }
    private void saveUserWallet(Long userId,Wallet wallet){
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE_WALLET_QUERY);
            ps.setLong(1, userId);
            ps.setInt(2, wallet.getBalance());
            return ps;
        });
    }

    @Override
    public Optional<User> findByUsername(String username) {

        return Optional.of(
                jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME,new Object[] {username},new UserMapper()));
    }

    @Override
    public Optional<Long> findUserIdByUsername(String username) {

        return Optional.of(
                jdbcTemplate.queryForObject(FIND_USER_ID_BY_USERNAME,new Object[] {username}, Long.class));
    }
}
