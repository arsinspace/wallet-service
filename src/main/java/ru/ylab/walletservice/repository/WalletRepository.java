package ru.ylab.walletservice.repository;
/**
 * Class provides operations for operating a wallet entity in Database
 */
public interface WalletRepository {
    /**
     * This method open connection to DB and update wallet entity in Database
     * @param userId Long userId
     * @param amount Integer new user balance
     */
    void updateBalance(long userId, int amount);

    /**
     * This method for find wallet in database by userId
     * @param userId Long
     * @return user balance
     */
    int findWalletByUserId(long userId);
}
