package ru.ylab.boot.repository;


import ru.ylab.boot.model.UserAction;

import java.sql.Timestamp;
import java.util.List;
/**
 * Class provides operations for operating a User actions entity in Database
 */
public interface UserActionRepository {
    /**
     * This method saving entity in Database
     * @param user_id Long userId
     * @param action String user action
     * @param time Timestamp time of action
     */
    void saveUserAction(long user_id, String action, String status, Timestamp time);

    /**
     * This method find all user actions entities in Database
     * @return List of all UserAction
     */
    List<UserAction> findAllUserActions();
}
