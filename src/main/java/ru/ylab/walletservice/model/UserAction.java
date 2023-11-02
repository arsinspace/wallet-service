package ru.ylab.walletservice.model;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder @ToString
public class UserAction {

    private long id;
    private long userId;
    private String action;
    private String status;
    private Timestamp time;
}
