package ru.ylab.walletservice.utils.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for load Properties from property file
 */
public class PropertiesPool {
    private static InputStream inputStream;
    private static final PropertiesPool PROPERTIES_POOL = new PropertiesPool();

    public static PropertiesPool getInstance(){
        return PROPERTIES_POOL;
    }
    public static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        properties.getProperty("url");
        return properties;
    }

    public void setInputStream(InputStream inputStream) {
        PropertiesPool.inputStream = inputStream;
    }
}
