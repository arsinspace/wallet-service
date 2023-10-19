package ru.ylab.utils.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for load Properties from property file
 */
public class PropertiesPool {
    public Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/database.properties"));
        properties.getProperty("url");
        return properties;
    }
}
