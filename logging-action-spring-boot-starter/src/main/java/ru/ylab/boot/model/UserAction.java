package ru.ylab.boot.model;

import lombok.*;

import java.sql.Timestamp;

/**
 * Class describes a User action
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserAction {

    private long id;
    private long userId;
    private String action;
    private String status;
    private Timestamp time;
}
