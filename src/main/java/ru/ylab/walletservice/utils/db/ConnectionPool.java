package ru.ylab.walletservice.utils.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class for create connection pool to Database with JDBC driver
 */
@PropertySource("classpath:application.yml")
public class ConnectionPool {
    /**
     * This field contains pool size for connection pool
     */
    @Value("${connection.poolSize}")
    private static int POOL_SIZE;
    /**
     * This field contains connection pool instance
     */
    private static ConnectionPool instance;
    /**
     * This field contains Database URL
     */
    @Value("${spring.datasource.url}")
    private static String URL;
    /**
     * This field contains Database username
     */
    @Value("${spring.datasource.username}")
    private static String USER;
    /**
     * This field contains Database password
     */
    @Value("${spring.datasource.password}")
    private static String PASSWORD;
    /**
     * This field contains connection BlockingQueue
     */
    private static BlockingQueue<Connection> pool;

    private ConnectionPool() throws ClassNotFoundException, IOException {
        Properties properties = PropertiesPool.getInstance().getProperties();
        String DRIVER = properties.getProperty("driver");
        Class.forName(DRIVER);
        POOL_SIZE = Integer.parseInt(properties.getProperty("poolSize"));
        URL = properties.getProperty("url");
        USER = properties.getProperty("username");
        PASSWORD = properties.getProperty("password");
        pool = new LinkedBlockingQueue<>(POOL_SIZE);
        initConnections();
    }

    public static ConnectionPool getInstanceConnection() throws SQLException, ClassNotFoundException, IOException {
        if (instance == null)
            instance = new ConnectionPool();
        return instance;
    }

    public static Connection getConnection() throws InterruptedException, SQLException {
            Connection connection = pool.take();
            if (!connection.isValid(0)) {
                connection.close();
                connection = createConnection();
            }
            return connection;
    }

    private void initConnections(){
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.offer(createConnection());
        }
    }

    private static Connection createConnection() {
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
                pool.put(connection);
            } catch (InterruptedException e) {
                System.out.println("Error: close connection -" + e);
            }
        }
    }
}
