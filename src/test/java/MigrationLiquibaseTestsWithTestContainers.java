import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Testcontainers
public class MigrationLiquibaseTestsWithTestContainers {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("testDB")
            .withUsername("test")
            .withPassword("test");

    @SneakyThrows
    @Test
    public void liquibaseMigrationTestWithTestContainers() {
        Connection connection = DriverManager.getConnection(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword());

        Statement statement = connection.createStatement();
        statement.execute("CREATE SCHEMA IF NOT EXISTS migration;CREATE SCHEMA IF NOT EXISTS wallet");

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("db/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
        database.setDefaultSchemaName("migration");
        liquibase.update();
        System.out.println("Миграции успешно выполнены!");

        Assertions.assertThat(statement.execute("select * from wallet.users where name = 'adam'")).isTrue();
        Assertions.assertThat(statement.execute("select * from wallet.credentials where username = 'adam'"))
                .isTrue();
        Assertions.assertThat(statement.execute("select * from wallet.transactions where user_id = 1")).isTrue();
        Assertions.assertThat(statement.execute("select * from wallet.user_action where action = 'Doing test'"))
                .isTrue();
        Assertions.assertThat(container.isRunning()).isTrue();
    }
}
