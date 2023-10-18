package ru.ylab.services.proxy;

import ru.ylab.logs.UserActionsAuditor;
import ru.ylab.model.User;
import ru.ylab.services.UserService;
import ru.ylab.services.impl.UserServiceImpl;
/**
 * @implNote The implementation is a Proxy class over the UserServiceImpl and is used to save user
 * actions into application memory before calling methods of the UserServiceImpl
 */
public class UserServiceProxy implements UserService {

    private final UserService userService = new UserServiceImpl();

    @Override
    public User processRegistration(String userJson) {
        UserActionsAuditor.writeAction("Registration new User " + userJson);
        return userService.processRegistration(userJson);
    }

    @Override
    public boolean processLogin(String credentialsJson) {
        boolean isLogin = userService.processLogin(credentialsJson);
        if (isLogin) {
            UserActionsAuditor.writeAction("User login success with credentials - " + credentialsJson);
            return true;
        } else {
            UserActionsAuditor.writeAction("Wrong credentials for - " + credentialsJson);
            return false;
        }
    }

    @Override
    public User getCurrentAppUser() {
        return userService.getCurrentAppUser();
    }

    @Override
    public boolean processLogout() {
        UserActionsAuditor.writeAction("User logout - " + userService.getCurrentAppUser());
        return userService.processLogout();
    }

    @Override
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    @Override
    public void getUserTransactionalHistory() {
        UserActionsAuditor.writeAction("User - " + "\n" + getCurrentAppUser() + "\n" +
                " - look his transactional history");
        userService.getUserTransactionalHistory();
    }

    @Override
    public void getUserWallet() {
        UserActionsAuditor.writeAction("User - " + "\n" + getCurrentAppUser() + "\n" + " - look his balance");
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
