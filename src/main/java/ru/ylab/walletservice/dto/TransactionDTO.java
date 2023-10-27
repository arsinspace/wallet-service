package ru.ylab.walletservice.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder @ToString
public class TransactionDTO {

    private String transactionalId;
    private String purpose;
    private int amount;
}
