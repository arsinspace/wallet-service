package ru.ylab.walletservice.dto;

import lombok.*;

/**
 * Data transfer class for Transaction
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TransactionDTO {

    private String transactionalId;
    private String purpose;
    private int amount;
}
