package ru.ylab.walletservice.model;

import lombok.*;
/**
 * Class describes a wallet
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Wallet {

    private int balance;

    @Override
    public String toString() {
        return "Balance: " + balance;
    }
}
