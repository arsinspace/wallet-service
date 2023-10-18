package ru.ylab.model;

import lombok.*;
import ru.ylab.model.enums.TransactionStatus;

import java.util.Date;
import java.util.UUID;
/**
 * Class describes a transaction
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class Transactional {
    private String transactionalId;
    private UUID userId;
    private String purpose;
    private int amount;
    private TransactionStatus status;
    private Date transactionalTime;
}
