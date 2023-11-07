package ru.ylab.walletservice.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ylab.walletservice.repository.WalletRepository;


/**
 * Class provides operations for operating a wallet entity in Database
 */
@Repository
@RequiredArgsConstructor
public class WalletDAO implements WalletRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * This field contains sql query for update wallet entity in DB
     */
    private static final String UPDATE_QUERY = "update wallet.wallet set balance = ? where user_id = ?";
    private static final String FIND_BY_USER_ID = "select balance from wallet.wallet where user_id = ?";

    /**
     * This method open connection to DB and update wallet entity in Database
     * @param userId Long userId
     * @param amount Integer new user balance
     */
     public void updateBalance(long userId, int amount){
        jdbcTemplate.update(UPDATE_QUERY, amount, userId);
    }

    public int findWalletByUserId(long userId){

        return jdbcTemplate.queryForObject(FIND_BY_USER_ID, Integer.class, userId);
    }
}
