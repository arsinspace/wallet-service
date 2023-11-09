package ru.ylab.walletservice.model;

import lombok.*;
import ru.ylab.walletservice.model.enums.TransactionStatus;

import java.sql.Timestamp;
/**
 * Class describes a transaction
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Transaction {
    private long id;
    private String transactionalId;
    private long userId;
    private String purpose;
    private int amount;
    private TransactionStatus status;
    private Timestamp transactionalTime;
}
