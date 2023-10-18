package ru.ylab.applicationMemory;

import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;

import java.util.*;

/**
 * @implNote Implementation with logic for working with users in application memory
 */
public class UserMemory implements UserRepository {

    private final Map<UUID, User> memory =  new HashMap<>();

    @Override
    public User findUserById(UUID id) {
        return memory.get(id);
    }

    @Override
    public User saveUser(User user) {
        return memory.put(user.getId(),user);
    }

    @Override
    public User updateUser(User user) {
        if (user == null) return null;
        return memory.replace(user.getId(),user);
    }

    @Override
    public User deleteUser(User user) {
        return memory.remove(user.getId());
    }

    @Override
    public User findUserByCredentials(Credentials credentials) {
       Optional<User> transientUser = memory.values()
                .stream()
                .filter(el -> el.getCredentials().toString().equals(credentials.toString()))
                .findAny();
       if (transientUser.isEmpty()) return null;
       else return transientUser.get();
    }

    @Override
    public List<User> findAllUsers() {
        return memory.values().stream().toList();
    }
}
