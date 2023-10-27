package ru.ylab.utils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.ylab.utils.db.ConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestLiquibase {

    public static void dbMigration() throws SQLException, IOException, ClassNotFoundException, InterruptedException {
            Connection  connection = null;
        try {
            connection = ConnectionPool.getInstanceConnection().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS migration;CREATE SCHEMA IF NOT EXISTS wallet");
        } catch (SQLException e) {
            System.out.println("Migration error - " + e);
        } finally {
            ConnectionPool.getInstanceConnection().closeConnection(connection);
        }

        try {
            connection = ConnectionPool.getInstanceConnection().getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
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
