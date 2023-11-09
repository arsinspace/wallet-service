package config.connections;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestConnectorToDb {

    private static TestConnectorToDb instance;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private TestConnectorToDb() {
        TestPropertiesPool propertiesPool = new TestPropertiesPool();
        Properties properties = null;
        try {
            properties = propertiesPool.getProperties();
        } catch (IOException e) {
            System.out.println("Error to create connection - " + e);
        }
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            System.out.println("Error to create connection - " + e);
        }
        URL = properties.getProperty("url");
        USER = properties.getProperty("username");
        PASSWORD = properties.getProperty("password");
    }

    public static TestConnectorToDb getInstance(){
        if (instance == null)
            instance = new TestConnectorToDb();
        return instance;
    }

    public static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            try {
                if (connection != null){
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error rollback: " + ex);
            }
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error: close connection -" + e);
            }
        }
    }
}
