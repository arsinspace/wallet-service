package config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainerEnvironment {

    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();


}
