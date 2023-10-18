package ru.ylab.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Class describes a user
 */
@Getter @Setter
@Builder
public class User {

    private UUID id;
    private String name;
    private String lastName;
    private String age;
    private final Wallet wallet = new Wallet();
    private Credentials credentials;
    private final List<Transactional> transactionalHistory = new ArrayList<>();

    public User(UUID id,String name, String lastName, String age,Credentials credentials) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.credentials = credentials;
    }

    public User() {
        id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "User(id=" + id + ", name=" + name + ", lastName=" + lastName + ", wallet=" + wallet + ")";
    }
}

