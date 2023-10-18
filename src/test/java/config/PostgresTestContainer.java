package config;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer>{

    public static final String IMAGE_VERSION = "postgres:11.1";
    public static final String DATABASE_NAME = "walletServiceDBtTest";
    public static PostgreSQLContainer container;

    private PostgresTestContainer(){
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainer getInstance(){
        if (container == null){
            container = new PostgresTestContainer().withDatabaseName(DATABASE_NAME);
        }
        return container;
    }

    @Override
    public void start(){
        super.start();
        System.setProperty("url",container.getJdbcUrl());
        System.setProperty("username",container.getUsername());
        System.setProperty("password",container.getPassword());
    }
    @Override
    public void stop(){

    }
}