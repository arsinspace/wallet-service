package ru.ylab.model;

import lombok.*;
/**
 * Class describes a wallet
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Wallet {

    private double balance;

    @Override
    public String toString() {
        return "Balance: " + balance;
    }
}
