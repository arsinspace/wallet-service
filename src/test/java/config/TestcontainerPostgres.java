package config;

import config.connections.TestConnectorToDb;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TestcontainerPostgres extends ContainerEnvironment{

    @Test
    public void testRepository(){

        Connection connection = TestConnectorToDb.getInstance().createConnection();
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
