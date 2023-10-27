package ru.ylab.walletservice.utils.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.ylab.walletservice.utils.db.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains configuration to Liquibase
 */
public class MigrationLiquibase {
    /**
     * Method start connection to database, create default schema for liquibase, create liquibase, set properties
     * and do migration to database
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static void dbMigration() throws SQLException, IOException, ClassNotFoundException, InterruptedException {
            Connection  connection = null;
        try {
            connection = ConnectionPool.getInstanceConnection().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS migration");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Migration error - " + e);
        } finally {
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        }

        try {
            connection = ConnectionPool.getInstanceConnection().getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("/WEB-INF/classes/db/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(), database);
            database.setDefaultSchemaName("migration");
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
        e.printStackTrace();
        } finally {
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        }
    }
}
