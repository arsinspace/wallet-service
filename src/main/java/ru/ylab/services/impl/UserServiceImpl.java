package ru.ylab.services.impl;

import ru.ylab.applicationMemory.UserMemory;
import ru.ylab.logs.UserActionsAuditor;
import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.repository.UserRepository;
import ru.ylab.services.UserService;
import ru.ylab.tools.JsonConverter;

import java.util.UUID;

/**
 * @implNote This implementation contains the logic for working with users
 */
public class UserServiceImpl implements UserService {
    /**
     * Field contains a link to the object UserMemory
     */
    private final UserRepository userMemory = new UserMemory();
    /**
     * Field contains a link to the current user object in UserService
     */
    private User currentAppUser;

    @Override
    public User processRegistration(String userJson) {
        User appUser = JsonConverter.convertToObject(User.class,userJson);
        userMemory.saveUser(appUser);
        currentAppUser = appUser;
        return appUser;
    }

    @Override
    public boolean processLogin(String credentialsJson) {
        Credentials userCredentials = JsonConverter.convertToObject(Credentials.class,credentialsJson);
        if (userCredentials.getLogin().equals("admin") && userCredentials.getPassword().equals("admin")){
            processLoginAdmin(userCredentials);
            return true;
        }
        User appUser = userMemory.findUserByCredentials(userCredentials);
        if (appUser != null) {
            currentAppUser = appUser;
            return true;
        }
        else return false;
    }

    /**
     * Process for login admin, create admin user
     * @param adminCredentials Credentials
     */
    public void processLoginAdmin(Credentials adminCredentials){
        currentAppUser = User.builder()
                .credentials(adminCredentials)
                .name("Admin")
                .id(UUID.randomUUID())
                .build();
    }

    @Override
    public User getCurrentAppUser() {
        return currentAppUser;
    }

    @Override
    public boolean processLogout() {
        try {
            if (currentAppUser != null){
                currentAppUser = null;
                return true;
            }
            else return false;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void updateUser(User user) {
        userMemory.updateUser(user);
    }

    @Override
    public void getUserTransactionalHistory() {
        currentAppUser.getTransactionalHistory().forEach(System.out::println);
    }

    @Override
    public void getUserWallet() {
        System.out.println("Your balance: " + currentAppUser.getWallet().getBalance());
    }

    @Override
    public void processAdminPanelViewAllUsers() {
        userMemory.findAllUsers().forEach(System.out::println);
    }

    @Override
    public void processAdminPanelViewUserActionsAuditor() {
        UserActionsAuditor.getRowData().forEach(System.out::println);
    }
}
