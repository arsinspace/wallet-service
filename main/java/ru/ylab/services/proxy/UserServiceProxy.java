package ru.ylab.services.proxy;

import ru.ylab.model.User;
import ru.ylab.repository.UserActionRepository;
import ru.ylab.services.UserService;

import java.sql.Timestamp;

/**
 * @implNote The implementation is a Proxy class over the UserServiceImpl and is used to save user
 * actions into application memory before calling methods of the UserServiceImpl
 */
public class UserServiceProxy implements UserService {

    private final UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User processRegistration(String userJson) {
        User user = userService.processRegistration(userJson);
        if (user != null){
            UserActionRepository.saveUserAction(user.getId(),
                    "user - " + user.getName() + " registration","success",
                    new Timestamp(System.currentTimeMillis()));
            return user;
        } else {
            UserActionRepository.saveUserAction(0L,
                    "user - none" + " registration","failed",
                    new Timestamp(System.currentTimeMillis()));
            return null;
        }
    }

    @Override
    public boolean processLogin(String credentialsJson) {
        boolean isLogin = userService.processLogin(credentialsJson);
        if (isLogin) {
            UserActionRepository.saveUserAction(0,
                    "login - " + credentialsJson,"success",
                    new Timestamp(System.currentTimeMillis()));
            return true;
        } else {
            UserActionRepository.saveUserAction(0,
                    "login - " + credentialsJson,"failed",
                    new Timestamp(System.currentTimeMillis()));
            return false;
        }
    }

    @Override
    public User getCurrentAppUser() {
        return userService.getCurrentAppUser();
    }

    @Override
    public boolean processLogout() {
        if (!userService.getCurrentAppUser().getName().startsWith("admin")){
            UserActionRepository.saveUserAction(userService.getCurrentAppUser().getId(),
                    "user id: " + userService.getCurrentAppUser().getId() + " - logout","success",
                    new Timestamp(System.currentTimeMillis()));
        }
        else {
            UserActionRepository.saveUserAction(0,
                    "user id:0 - logout","success",
                    new Timestamp(System.currentTimeMillis()));
        }
        return userService.processLogout();
    }

    @Override
    public void getUserWallet() {
        UserActionRepository.saveUserAction(userService.getCurrentAppUser().getId(),
                "user id: " + userService.getCurrentAppUser().getId() + " - look his balance",
                "success",
                new Timestamp(System.currentTimeMillis()));
        userService.getUserWallet();
    }

    @Override
    public void processAdminPanelViewAllUsers() {
        userService.processAdminPanelViewAllUsers();
    }

    @Override
    public void processAdminPanelViewUserActionsAuditor() {
        userService.processAdminPanelViewUserActionsAuditor();
    }
}
