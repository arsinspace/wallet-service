package config.connections;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestPropertiesPool {
    public Properties getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/test/resources/db.properties"));
        properties.getProperty("testUrl");
        return properties;
    }
}
