package ru.ylab.model;

import lombok.*;
import ru.ylab.model.enums.TransactionStatus;

import java.sql.Timestamp;
/**
 * Class describes a transaction
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
@Builder
public class Transactional {
    private long id;
    private String transactionalId;
    private long userId;
    private String purpose;
    private int amount;
    private TransactionStatus status;
    private Timestamp transactionalTime;
}
