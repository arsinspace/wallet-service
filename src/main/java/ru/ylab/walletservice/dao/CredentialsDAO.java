package ru.ylab.walletservice.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ylab.walletservice.repository.CredentialsRepository;

/**
 * Class provides operations for operating a user credentials entity in Database
 */
@Repository
@RequiredArgsConstructor
public class CredentialsDAO implements CredentialsRepository {

    private final JdbcTemplate jdbcTemplate;
    /**
     * This field contains sql query for find user credentials entity in DB
     */
    private final String FIND_CREDENTIAL_BY_USERNAME = "select count(*) from wallet.credentials where username = ?";

    /**
     * This method checking is login used in Database
     * @param userName String users username
     * @return boolean result of checking
     */
    public boolean isLoginUsed(String userName){

       Integer count = jdbcTemplate.queryForObject(FIND_CREDENTIAL_BY_USERNAME,
               new Object[]{userName}, Integer.class);
       return count != null && count > 0;
    }
}
