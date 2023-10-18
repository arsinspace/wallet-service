package ru.ylab.logs;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for save users actions
 */
public class UserActionsAuditor {

    private static UserActionsAuditor instance;
    private static List<String> rowData = new ArrayList<>();
    private UserActionsAuditor(){

    }

    public static UserActionsAuditor getInstance(){
        if (instance == null)
            instance = new UserActionsAuditor();
        return instance;
    }
    public static void writeAction(String action){
        rowData.add(action);
    }

    public static List<String> getRowData(){
        return rowData;
    }
}
