package ru.ylab.walletservice.utils.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.ylab.walletservice.model.Wallet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletMapper implements RowMapper<Wallet> {
    @Override
    public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Wallet.builder()
                .balance(rs.getInt("balance"))
                .build();
    }
}
