package ru.ylab.services.impl;

import ru.ylab.model.Credentials;
import ru.ylab.model.User;
import ru.ylab.model.Wallet;
import ru.ylab.repository.CredentialsRepository;
import ru.ylab.repository.UserActionRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.services.UserService;
import ru.ylab.utils.JsonConverter;

import java.util.Optional;

/**
 * @implNote This implementation contains the logic for working with users
 */
public class UserServiceImpl implements UserService {
    /**
     * Field contains a link to the object UserMemory
     */
    private final UserRepository userDAO;
    /**
     * Field contains a link to the current user object in UserService
     */
    private User currentAppUser;

    public UserServiceImpl(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User processRegistration(String userJson) {
        User appUser = JsonConverter.convertToObject(User.class,userJson);
        if (appUser != null && !CredentialsRepository.isLoginUsed(appUser.getCredentials().getLogin())) {
                    long userId = userDAO.saveUser(appUser);
                    appUser.setId(userId);
                    appUser.setWallet(new Wallet(0));
                    currentAppUser = appUser;
                    return appUser;
            } else {
                System.out.println("Registration error");
            return null;
        }
    }

    @Override
    public boolean processLogin(String credentialsJson) {
        Credentials userCredentials = JsonConverter.convertToObject(Credentials.class,credentialsJson);
        if (userCredentials!= null && userCredentials.getLogin().equals("admin")
                && userCredentials.getPassword().equals("admin")){
            processLoginAdmin(userCredentials);
            return true;
        } else if (userCredentials != null){
            Optional<User> appUser = userDAO.findUserByCredentials(userCredentials);
            if (appUser.isPresent()) {
                currentAppUser = appUser.get();
                System.out.println(appUser.get());
                return true;
            }
            return false;
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
    public void getUserWallet() {
        System.out.println("Your balance: " + currentAppUser.getWallet().getBalance());
    }

    @Override
    public void processAdminPanelViewAllUsers() {
        userDAO.findAllUsers().forEach(System.out::println);
    }

    @Override
    public void processAdminPanelViewUserActionsAuditor() {
        UserActionRepository.findAllUserActions().forEach(System.out::println);
    }
}
