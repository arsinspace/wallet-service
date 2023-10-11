package ru.ylab.model;

import lombok.*;
/**
 * Class describes a users credential
 */
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder @ToString
public class Credentials {

    private String login;
    private String password;
}
