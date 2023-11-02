package ru.ylab.walletservice.model;

import lombok.*;

/**
 * Class describes a user
 */
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class User {

    private long id;
    private String name;
    private String lastName;
    private String age;
    private Wallet wallet;
    private Credentials credentials;

    @Override
    public String toString() {
        return "User(id=" + id + ", name=" + name + ", lastName=" + lastName + ", wallet=" + wallet + ")";
    }
}

